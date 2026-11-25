package com.alvaro.tracker.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.alvaro.tracker.model.Lot;
import com.alvaro.tracker.model.LotConsumption;

@Component 
public class FifoCalculator {
   public List<LotConsumption> sell(List<Lot> lots, BigDecimal quantity, BigDecimal price) {
      List<LotConsumption> consumptions = new ArrayList<>();
      BigDecimal pending = quantity; // Cantidad que queda por vender

      for (Lot lot : lots) {
         if(pending.compareTo(BigDecimal.ZERO) == 0) {
            break;
         }
         if(lot.getRemainingQuantity().compareTo(BigDecimal.ZERO) == 0) {
            continue;
         }
         BigDecimal taken = pending.min(lot.getRemainingQuantity()); // Cantidad de acciones que se van a sacar de ese lote
         BigDecimal cost = taken.multiply(lot.getPrice()); // Coste de las acciones vendida de este lote
         BigDecimal proceeds = taken.multiply(price); // Cantidad recibida por ventas
         BigDecimal result = proceeds.subtract(cost); // Ganancias

         lot.consume(taken);
         pending = pending.subtract(taken);
         consumptions.add(new LotConsumption(lot, taken, cost, proceeds, result));
      }
      return consumptions;
   }
}
