package com.alvaro.tracker.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.alvaro.tracker.model.Invested;
import com.alvaro.tracker.model.Operation;
import com.alvaro.tracker.model.OperationRequest;
import com.alvaro.tracker.model.OperationType;
import com.alvaro.tracker.model.Position;

import jakarta.validation.Valid;


@RestController 
@RequestMapping("/api/operations")
public class OperationController {

   private final List<Operation> operations = new ArrayList<>();
   private final AtomicLong nextId = new AtomicLong(1);

   // Listado general de operaciones
   @GetMapping 
   public List<Operation> findAll () {
      return operations;
   }

   // Listado de posiciones del mismo ticker
   @GetMapping("/{ticker}/position")
   public Position getPosition(@PathVariable String ticker) {
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

   // Listado de posiciones del mismo ticker
   @GetMapping("/{ticker}/invested")
   public Invested getInvested(@PathVariable String ticker) {
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

   // Creacion de operacion y validacion en caso de venta
   @PostMapping 
   @ResponseStatus(HttpStatus.CREATED)
   public Operation create(@Valid @RequestBody OperationRequest request) {

      if(request.type() == OperationType.SELL) {
         Position position = getPosition(request.ticker());
         if(request.quantity().compareTo(position.quantity()) > 0) {
            throw new IllegalArgumentException("No puedes vender " + request.quantity() + " " + request.ticker() + ": solo tienes " + position.quantity());
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
