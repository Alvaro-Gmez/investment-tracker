package com.alvaro.tracker;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Operation (
   Long id, 
   String ticker,
   OperationType type,
   BigDecimal quantity,
   BigDecimal price,
   LocalDate date) {
   
}
