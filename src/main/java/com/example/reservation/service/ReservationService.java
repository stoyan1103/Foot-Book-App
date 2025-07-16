package com.example.reservation.service;

import com.example.reservation.model.Reservation;
import com.example.web.dto.CreateReservationRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationService {

    Reservation createReservation(Long userId, CreateReservationRequest createReservationRequest);

    boolean isFieldAvailable(Long fieldId, LocalDateTime startTime, LocalDateTime endTime);

    List<Reservation> getActiveReservationsBefore(LocalDateTime now);

    void updateReservation(Reservation reservation);
}