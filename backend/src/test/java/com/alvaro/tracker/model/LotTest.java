package com.alvaro.tracker.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class LotTest {
   
   @Test
   void consume_restaCantidadRestante () {
      // Arrange: Lote de 10 acciones
      Lot lot = new Lot("AAPL", LocalDate.of(2026, 10, 10), new BigDecimal("150"), new BigDecimal("10"));

      // Act: Consume 4
      lot.consume(new BigDecimal("4"));
      
      // Assert: Quedan 6 y la cantidad inicial sigue siendo 10
      assertThat(lot.getRemainingQuantity()).isEqualByComparingTo("6");
      assertThat(lot.getInitialQuantity()).isEqualByComparingTo("10");
   }

   @Test 
   void consume_masDeLoQueQueda_lanzaExcepcion() {
      // Arrange: Lote de 5 acciones
      Lot lot = new Lot("AAPL", LocalDate.of(2026, 10, 10), new BigDecimal("150"), new BigDecimal("10"));

      // Act + Assert: Consume 20 y lanza excepcion
      assertThatThrownBy(() -> lot.consume(new BigDecimal("20"))).isInstanceOf(IllegalArgumentException.class);      
   }
}
