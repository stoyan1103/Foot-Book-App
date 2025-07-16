package com.example.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateReservationRequest {

    @NotNull(message = "Start time is required.")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required.")
    private LocalDateTime endTime;

    @NotNull(message = "Field ID is required.")
    private Long fieldId;

    private boolean payNow;
}