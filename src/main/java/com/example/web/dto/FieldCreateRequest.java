package com.example.web.dto;

import com.example.field.model.City;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalTime;

@Builder
@Data
public class FieldCreateRequest {

    @NotNull(message = "Name cannot be null")
    @Size(min = 1, message = "Field name should have at least 1 symbol.")
    private String name;

    @NotNull
    private City city;

    @NotBlank(message = "Моля, въведете URL за локацията.")
    private String locationUrl;

    @NotNull(message = "Моля, въведете час на отваряне.")
    private LocalTime openHour;

    @NotNull(message = "Моля, въведете час на затваряне.")
    private LocalTime closeHour;

    @NotBlank(message = "Моля, добавете изображение.")
    private String imageUrl;

    private String description;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal pricePerHour;
}