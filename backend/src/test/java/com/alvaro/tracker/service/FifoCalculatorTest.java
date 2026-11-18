package com.alvaro.tracker.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.alvaro.tracker.model.Lot;
import com.alvaro.tracker.model.LotConsumption;

public class FifoCalculatorTest {
   
   @Test 
   void sell_ventaParcialDeUnLote() { 
      // Arrange
      Lot loteA = new Lot("AAPL", LocalDate.of(2026, 1, 10), new BigDecimal("100"), new BigDecimal("10"));
      FifoCalculator calculator = new FifoCalculator();

      // Act
      List<LotConsumption> result = calculator.sell(List.of(loteA), new BigDecimal("4"), new BigDecimal("400"));

      // Assert
      assertThat(result).hasSize(1);
      LotConsumption c = result.get(0);
      assertThat(c.quantity()).isEqualByComparingTo("4");
      assertThat(c.cost()).isEqualByComparingTo("400");
   }
}
