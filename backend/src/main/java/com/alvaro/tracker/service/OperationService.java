package com.alvaro.tracker.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.alvaro.tracker.exception.InsufficientPositionException;
import com.alvaro.tracker.model.Count;
import com.alvaro.tracker.model.Invested;
import com.alvaro.tracker.model.Operation;
import com.alvaro.tracker.model.OperationRequest;
import com.alvaro.tracker.model.OperationType;
import com.alvaro.tracker.model.Position;

@Service 
public class OperationService {
   
   private final List<Operation> operations = new ArrayList<>();
   private final AtomicLong nextId = new AtomicLong(1);

   // Listado general de operaciones
   public List<Operation> findAll () {
      return List.copyOf(operations);
   }

   // Posiciones del mismo ticker
   public Position getPosition(String ticker) {
      BigDecimal total = BigDecimal.ZERO;

      for (Operation operation : operations) {
         if (operation.ticker().equalsIgnoreCase(ticker)) {
            if(operation.type() == OperationType.BUY) {
               total = total.add(operation.quantity());
            } else {
               total = total.subtract(operation.quantity());
            }
         }
      }
      return new Position(ticker.toUpperCase(), total);
   }

   // Posiciones invertidas del mismo ticker
   public Invested getInvested(String ticker) {
      BigDecimal total = BigDecimal.ZERO;

      for (Operation operation : operations) {
         if (operation.ticker().equalsIgnoreCase(ticker)) {
            if(operation.type() == OperationType.BUY) {
               total = total.add(operation.price().multiply(operation.quantity()));
            } 
         }
      }
      return new Invested(ticker.toUpperCase(), total);
   }

   // Conteo de las operaciones del mismo ticker
   public Count getCount(String ticker) {
      int operacionesTotales = 0;

      for(Operation operation : operations) {
         if(operation.ticker().equalsIgnoreCase(ticker)) {
            operacionesTotales += 1;
         }
      }

      return new Count(ticker.toUpperCase(), operacionesTotales);
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
      nextId.getAndIncrement(),
      request.ticker().toUpperCase(),
      request.type(),
      request.quantity(),
      request.price(),
      request.date());
   
      operations.add(operation);
      return operation;
   }
}
