package com.alvaro.tracker;

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

import jakarta.validation.Valid;


@RestController 
@RequestMapping("/api/operations")
public class OperationController {

   private final List<Operation> operations = new ArrayList<>();
   private final AtomicLong nextId = new AtomicLong(1);

   @GetMapping 
   public List<Operation> findAll () {
      return operations;
   }

   @GetMapping("/{ticker}/position")
   public Position gePosition(@PathVariable String ticker) {
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
      return new Position(ticker, total);
   }


   @PostMapping 
   @ResponseStatus(HttpStatus.CREATED)
   public Operation create(@Valid @RequestBody OperationRequest request) {
      Operation operation = new Operation(
         nextId.getAndIncrement(),
         request.ticker(),
         request.type(),
         request.quantity(),
         request.price(),
         request.date());
      operations.add(operation);
      return operation;
   }
}
