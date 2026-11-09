package com.alvaro.tracker.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.alvaro.tracker.exception.InsufficientPositionException;
import com.alvaro.tracker.model.Count;
import com.alvaro.tracker.model.Invested;
import com.alvaro.tracker.model.Operation;
import com.alvaro.tracker.model.OperationRequest;
import com.alvaro.tracker.model.OperationType;
import com.alvaro.tracker.model.Position;

import com.alvaro.tracker.repository.OperationRepository;

@Service 
public class OperationService {

   private final OperationRepository operationRepository;

   public OperationService (OperationRepository operationRepository) {
      this.operationRepository = operationRepository; 
   }

   // Listado general de operaciones
   public List<Operation> findAll () {
      return operationRepository.findAll();
   }

   // Posiciones del mismo ticker
   public Position getPosition(String ticker) {
      BigDecimal total = BigDecimal.ZERO;

      for (Operation operation : operationRepository.findAll()) {
         if (operation.getTicker().equalsIgnoreCase(ticker)) {
            if(operation.getType() == OperationType.BUY) {
               total = total.add(operation.getQuantity());
            } else {
               total = total.subtract(operation.getQuantity());
            }
         }
      }
      return new Position(ticker.toUpperCase(), total);
   }

   // Posiciones invertidas del mismo ticker
   public Invested getInvested(String ticker) {
      BigDecimal total = BigDecimal.ZERO;

      for (Operation operation : operationRepository.findAll()) {
         if (operation.getTicker().equalsIgnoreCase(ticker)) {
            if(operation.getType() == OperationType.BUY) {
               total = total.add(operation.getPrice().multiply(operation.getQuantity()));
            } 
         }
      }
      return new Invested(ticker.toUpperCase(), total);
   }

   // Conteo de las operaciones del mismo ticker
   public Count getCount(String ticker) {
      int count = 0;

      for(Operation operation : operationRepository.findAll()) {
         if(operation.getTicker().equalsIgnoreCase(ticker)) {
            count += 1;
         }
      }
      return new Count(ticker.toUpperCase(), count);
   }

   // Creacion de operacion y validacion en caso de venta
   public Operation create(OperationRequest request) {
      if(request.type() == OperationType.SELL) {
         Position position = getPosition(request.ticker());
         if(request.quantity().compareTo(position.quantity()) > 0) {
            throw new InsufficientPositionException("No puedes vender " + request.quantity() + " " + request.ticker().toUpperCase() + ": solo tienes " + position.quantity());
         }
      }
      Operation operation = new Operation(
      request.ticker().toUpperCase(),
      request.type(),
      request.quantity(),
      request.price(),
      request.date());

      return operationRepository.save(operation);
   }
}
