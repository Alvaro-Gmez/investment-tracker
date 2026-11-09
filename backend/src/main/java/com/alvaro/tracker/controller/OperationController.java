package com.alvaro.tracker.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.alvaro.tracker.model.Count;
import com.alvaro.tracker.model.Invested;
import com.alvaro.tracker.model.Operation;
import com.alvaro.tracker.model.OperationRequest;
import com.alvaro.tracker.model.Position;
import com.alvaro.tracker.service.OperationService;

import jakarta.validation.Valid;

@RestController 
@RequestMapping("/api/operations")
public class OperationController {
   
   private final OperationService operationService;

   public OperationController (OperationService operationService) {
      this.operationService = operationService;
   }

   @GetMapping
   public List<Operation> findAll() {
      return operationService.findAll();
   }

   @GetMapping("/{ticker}/position")
   public Position getPosition(@PathVariable String ticker) {
      return operationService.getPosition(ticker);
   }

   @GetMapping("/{ticker}/invested")
   public Invested getInvested(@PathVariable String ticker) {
      return operationService.getInvested(ticker);
   }

   @GetMapping("/{ticker}/count")
   public Count getCount(@PathVariable String ticker) {
      return operationService.getCount(ticker);
   }

   @PostMapping 
   @ResponseStatus(HttpStatus.CREATED)
   public Operation create(@Valid @RequestBody OperationRequest request) {
      return operationService.create(request);
   }
}
