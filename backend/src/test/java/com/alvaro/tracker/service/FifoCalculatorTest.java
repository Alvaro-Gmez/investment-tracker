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
   void sell_ventaParcialDeUnLote_consumeSoloEseLote() { // Caso 1
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
      assertThat(loteA.getRemainingQuantity()).isEqualByComparingTo("6");
   }

   @Test 
   void sell_ventaQueSuperaUnLote_consumeDosLotesEnOrden() { // Caso 2
      // Arrange
      Lot loteA = new Lot("AAPL", LocalDate.of(2026, 2, 1), new BigDecimal("50"), new BigDecimal("10"));
      Lot loteB = new Lot("AAPL", LocalDate.of(2026, 3, 1), new BigDecimal("60"), new BigDecimal("10"));
      FifoCalculator calculator = new FifoCalculator();

      // Act
      List<LotConsumption> result = calculator.sell(List.of(loteA, loteB), new BigDecimal("15"), new BigDecimal("70"));

      // Assert
      assertThat(result).hasSize(2);
      LotConsumption a = result.get(0);
      assertThat(a.quantity()).isEqualByComparingTo("10");
      assertThat(a.cost()).isEqualByComparingTo("500");
      assertThat(a.proceeds()).isEqualByComparingTo("700");
      assertThat(a.result()).isEqualByComparingTo("200");
      assertThat(loteA.getRemainingQuantity()).isEqualByComparingTo("0");
      LotConsumption b = result.get(1);
      assertThat(b.quantity()).isEqualByComparingTo("5");
      assertThat(b.cost()).isEqualByComparingTo("300");
      assertThat(b.proceeds()).isEqualByComparingTo("350");
      assertThat(b.result()).isEqualByComparingTo("50");
      assertThat(loteB.getRemainingQuantity()).isEqualByComparingTo("5");
   }

   @Test
   void sell_precioDeVentaMenorQueCompra_resultadoNegativo() { // Caso 3
      // Arrange
      Lot loteA = new Lot("AAPL", LocalDate.of(2026, 2, 1), new BigDecimal("40"), new BigDecimal("20"));
      FifoCalculator calculator = new FifoCalculator();

      // Act
      List<LotConsumption> result = calculator.sell(List.of(loteA), new BigDecimal("5"), new BigDecimal("30"));

      // Assert
      assertThat(result).hasSize(1);
      LotConsumption a = result.get(0);
      assertThat(a.quantity()).isEqualByComparingTo("5");
      assertThat(a.cost()).isEqualByComparingTo("200");
      assertThat(a.proceeds()).isEqualByComparingTo("150");
      assertThat(a.result()).isEqualByComparingTo("-50");
      assertThat(loteA.getRemainingQuantity()).isEqualByComparingTo("15");
   }

   @Test
   void sell_dosVentasSeguidas_laSegundaContinuaDondeAcaboLaPrimera() { // Caso 4
      // Arrange
      Lot loteA = new Lot("AAPL", LocalDate.of(2026, 1, 1), new BigDecimal("20"), new BigDecimal("5"));
      Lot loteB = new Lot("AAPL", LocalDate.of(2026, 2, 1), new BigDecimal("30"), new BigDecimal("5"));
      Lot loteC = new Lot("AAPL", LocalDate.of(2026, 3, 1), new BigDecimal("40"), new BigDecimal("5"));
      FifoCalculator calculator = new FifoCalculator();

      // Act
      List<LotConsumption> venta1 = calculator.sell(List.of(loteA, loteB, loteC), new BigDecimal("7"), new BigDecimal("35"));
      List<LotConsumption> venta2 = calculator.sell(List.of(loteA, loteB, loteC), new BigDecimal("6"), new BigDecimal("25"));

      // Assert
      assertThat(venta1).hasSize(2);
      LotConsumption a = venta1.get(0);
      assertThat(a.quantity()).isEqualByComparingTo("5");
      assertThat(a.cost()).isEqualByComparingTo("100");
      assertThat(a.proceeds()).isEqualByComparingTo("175");
      assertThat(a.result()).isEqualByComparingTo("75");
      LotConsumption b = venta1.get(1);
      assertThat(b.quantity()).isEqualByComparingTo("2");
      assertThat(b.cost()).isEqualByComparingTo("60");
      assertThat(b.proceeds()).isEqualByComparingTo("70");
      assertThat(b.result()).isEqualByComparingTo("10");

      assertThat(venta2).hasSize(2);
      LotConsumption c = venta2.get(0);
      assertThat(c.quantity()).isEqualByComparingTo("3");
      assertThat(c.cost()).isEqualByComparingTo("90");
      assertThat(c.proceeds()).isEqualByComparingTo("75");
      assertThat(c.result()).isEqualByComparingTo("-15");
      LotConsumption d = venta2.get(1);
      assertThat(d.quantity()).isEqualByComparingTo("3");
      assertThat(d.cost()).isEqualByComparingTo("120");
      assertThat(d.proceeds()).isEqualByComparingTo("75");
      assertThat(d.result()).isEqualByComparingTo("-45");

      assertThat(loteA.getRemainingQuantity()).isEqualByComparingTo("0");
      assertThat(loteB.getRemainingQuantity()).isEqualByComparingTo("0");
      assertThat(loteC.getRemainingQuantity()).isEqualByComparingTo("2");
   }

   @Test 
   void sell_venderTodoAlMismoPrecio_resultadoCeroYLoteVacio() { // Caso 5
      // Arrange
      Lot loteA = new Lot("AAPL", LocalDate.of(2026, 1, 1), new BigDecimal("100"), new BigDecimal("10"));
      FifoCalculator calculator = new FifoCalculator();

      // Act
      List<LotConsumption> result = calculator.sell(List.of(loteA), new BigDecimal("10"), new BigDecimal("100"));

      // Assert
      assertThat(result).hasSize(1);
      LotConsumption a = result.get(0);
      assertThat(a.quantity()).isEqualByComparingTo("10");
      assertThat(a.cost()).isEqualByComparingTo("1000");
      assertThat(a.proceeds()).isEqualByComparingTo("1000");
      assertThat(a.result()).isEqualByComparingTo("0");
      assertThat(loteA.getRemainingQuantity()).isEqualByComparingTo("0");
   }
}
