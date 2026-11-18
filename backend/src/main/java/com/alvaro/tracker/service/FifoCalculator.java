package com.alvaro.tracker.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import com.alvaro.tracker.model.Lot;
import com.alvaro.tracker.model.LotConsumption;

@Component 
public class FifoCalculator {
   public List<LotConsumption> sell(List<Lot> lots, BigDecimal quantity, BigDecimal price) {
      throw new UnsupportedOperationException("Por implementar");
   }
}
