package com.example.reservation.service;

import com.example.exception.FieldNotAvailableException;
import com.example.exception.InvalidReservationHoursException;
import com.example.field.model.Field;
import com.example.field.service.FieldService;
import com.example.reservation.model.Reservation;
import com.example.reservation.repository.ReservationRepository;
import com.example.transaction.model.Transaction;
import com.example.user.model.User;
import com.example.user.service.UserService;
import com.example.wallet.service.WalletService;
import com.example.web.dto.CreateReservationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserService userService;
    private final FieldService fieldService;
    private final WalletService walletService;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                  UserService userService,
                                  FieldService fieldService,
                                  WalletService walletService) {
        this.reservationRepository = reservationRepository;
        this.userService = userService;
        this.fieldService = fieldService;
        this.walletService = walletService;
    }

    @Override
    @Transactional
    public Reservation createReservation(Long userId, CreateReservationRequest createReservationRequest) {
        Field field = fieldService.getFieldById(createReservationRequest.getFieldId());

        validateReservationTime(createReservationRequest.getStartTime(), createReservationRequest.getEndTime());

        checkIsFieldAvailable(field);
        checkIsFieldAlreadyReserved(field, createReservationRequest.getStartTime(), createReservationRequest.getEndTime());

        User reservationUser = userService.getById(userId);
        Reservation reservation = createReservation(reservationUser, createReservationRequest, field);

        if (createReservationRequest.isPayNow()) {
            Transaction transaction = chargeUser(reservationUser, field, createReservationRequest);

            reservation.setTransaction(transaction);
        }

        return reservationRepository.save(reservation);
    }

    private void validateReservationTime(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime.isBefore(LocalDateTime.now())) {
            throw new InvalidReservationHoursException("Cannot make reservation for a past time.");
        }

        if (endTime.isBefore(startTime) || endTime.equals(startTime)) {
            throw new InvalidReservationHoursException("End time must be after start time.");
        }
    }

    private void checkIsFieldAvailable(Field field) {
        if (!field.isAvailable() || !field.isApproved()) {
            throw new FieldNotAvailableException("Field is not available for reservations.");
        }
    }

    private void checkIsFieldAlreadyReserved(Field field, LocalDateTime requestedStartTime, LocalDateTime requestedEndTime) {
        boolean isReserved = reservationRepository.existsByFieldAndTimeOverlapAndIsExpiredFalse(field, requestedEndTime, requestedStartTime);

        if (isReserved) {
            throw new FieldNotAvailableException("Field is already reserved during the selected time slot.");
        }
    }

    private Transaction chargeUser(User reservationUser, Field field, CreateReservationRequest createReservationRequest) {
        String description = "Paid reservation for field [%s] from %s to %s.".formatted(
                field.getName(),
                createReservationRequest.getStartTime(),
                createReservationRequest.getEndTime()
        );

        Transaction transaction = walletService.charge(
                reservationUser,
                reservationUser.getWallet().getId(),
                field.getPricePerHour(),
                description
        );

        return transaction;
    }

    private Reservation createReservation(User reservationUser, CreateReservationRequest createReservationRequest, Field field) {
        return Reservation.builder()
                .startTime(createReservationRequest.getStartTime())
                .endTime(createReservationRequest.getEndTime())
                .isExpired(false)
                .user(reservationUser)
                .field(field)
                .build();
    }

    @Override
    public boolean isFieldAvailable(Long fieldId, LocalDateTime startTime, LocalDateTime endTime) {
        Field field = fieldService.getFieldById(fieldId);

        return !reservationRepository.existsByFieldAndStartTime(field, startTime);
    }

    @Override
    public List<Reservation> getActiveReservationsBefore(LocalDateTime dateTime) {
        return reservationRepository.findByEndTimeBeforeAndIsExpiredFalse(dateTime);
    }

    public void updateReservation(Reservation reservation) {
        reservationRepository.save(reservation);
    }
}