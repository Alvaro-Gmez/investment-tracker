package com.alvaro.tracker.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

public record OperationRequest (
   @NotBlank String ticker,
   @NotNull OperationType type,
   @NotNull @Positive BigDecimal quantity,
   @NotNull @Positive BigDecimal price,
   @NotNull @PastOrPresent LocalDate date) {
   
}
