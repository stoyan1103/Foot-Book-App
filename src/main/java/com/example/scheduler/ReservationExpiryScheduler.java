package com.example.scheduler;

import com.example.reservation.model.Reservation;
import com.example.reservation.service.ReservationService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class ReservationExpiryScheduler {

    private final ReservationService reservationService;

    @Autowired
    public ReservationExpiryScheduler(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Scheduled(cron = "1 0 * * * *")
    @Transactional
    public void expirePastReservations() {
        LocalDateTime now = LocalDateTime.now();
        List<Reservation> activeReservations = reservationService.getActiveReservationsBefore(now);

        for (Reservation reservation : activeReservations) {
            reservation.setExpired(true);
            reservationService.updateReservation(reservation);
        }

        log.info("%d reservations expired.".formatted(activeReservations.size()));
    }


}