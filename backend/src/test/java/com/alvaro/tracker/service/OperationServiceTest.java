package com.alvaro.tracker.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.alvaro.tracker.exception.InsufficientPositionException;
import com.alvaro.tracker.model.Invested;
import com.alvaro.tracker.model.Operation;
import com.alvaro.tracker.model.OperationRequest;
import com.alvaro.tracker.model.OperationType;
import com.alvaro.tracker.model.Position;
import com.alvaro.tracker.repository.OperationRepository;

@ExtendWith(MockitoExtension.class)
class OperationServiceTest {

   @Mock
   private OperationRepository operationRepository;

   @InjectMocks
   private OperationService operationService;

   @Test
   void getPosition_sumaComprasYRestaVentas() {
      // Arrange: El repositorio false devolverá estas 3 opciones
      when(operationRepository.findByTicker("AAPL")).thenReturn(List.of(
         new Operation("AAPL", OperationType.BUY, new BigDecimal("10"), new BigDecimal("150"), LocalDate.of(2026, 10, 1)),
         new Operation("AAPL", OperationType.BUY, new BigDecimal("5"), new BigDecimal("160"), LocalDate.of(2026, 10, 5)),
         new Operation("AAPL", OperationType.SELL, new BigDecimal("3"), new BigDecimal("170"), LocalDate.of(2026, 10, 10))
      ));

      // Act
      Position position = operationService.getPosition("aapl");

      // Assert
      assertThat(position.ticker()).isEqualTo("AAPL");
      assertThat(position.quantity()).isEqualByComparingTo("12");
   }

   @Test
   void create_ventaMayorQuePosicion_lanzaExcepcion() { 
      // Arrange: Devuelve una sola compra de 10 acciones
      when(operationRepository.findByTicker("AAPL")).thenReturn(List.of(
         new Operation("AAPL", OperationType.BUY, new BigDecimal("10"), new BigDecimal("150"), LocalDate.of(2026, 10, 1))
      ));
      OperationRequest request = new OperationRequest("AAPL", OperationType.SELL, new BigDecimal("20"), new BigDecimal("160"), LocalDate.of(2026, 10, 10));
      
      // Act + Assert
      assertThatThrownBy(() -> operationService.create(request))
         .isInstanceOf(InsufficientPositionException.class);
   }

   @Test
   void getInvested_sumaCantidadXPrecio_soloCompras() {
      // Arrange: Devuelve dos compras y una venta
      when(operationRepository.findByTicker("AAPL")).thenReturn(List.of(
         new Operation("AAPL", OperationType.BUY, new BigDecimal("10"), new BigDecimal("150"), LocalDate.of(2026, 10, 1)),
         new Operation("AAPL", OperationType.BUY, new BigDecimal("5"), new BigDecimal("160"), LocalDate.of(2026, 10, 1)),
         new Operation("AAPL", OperationType.SELL, new BigDecimal("3"), new BigDecimal("170"), LocalDate.of(2026, 10, 1))
      ));

      //Act
      Invested invested = operationService.getInvested("aapl");

      //Assert
      assertThat(invested.ticker()).isEqualTo("AAPL");
      assertThat(invested.invested()).isEqualByComparingTo("2300");
   }
}
