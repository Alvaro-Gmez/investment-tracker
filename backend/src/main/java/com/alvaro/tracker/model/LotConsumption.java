package com.alvaro.tracker.model;

import java.math.BigDecimal;

public record LotConsumption(
   Lot lot, 
   BigDecimal quantity, 
   BigDecimal cost, 
   BigDecimal proceeds, // Recibido por ventas 
   BigDecimal result) {

}
