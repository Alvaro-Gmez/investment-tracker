package com.alvaro.tracker.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alvaro.tracker.exception.InsufficientPositionException;
import com.alvaro.tracker.model.Count;
import com.alvaro.tracker.model.Invested;
import com.alvaro.tracker.model.Lot;
import com.alvaro.tracker.model.Operation;
import com.alvaro.tracker.model.OperationRequest;
import com.alvaro.tracker.model.OperationType;
import com.alvaro.tracker.model.Position;
import com.alvaro.tracker.repository.LotRepository;
import com.alvaro.tracker.repository.OperationRepository;

@Service 
public class OperationService {

   private final OperationRepository operationRepository;
   private final LotRepository lotRepository;
   private final FifoCalculator fifoCalculator;

   public OperationService (OperationRepository operationRepository, LotRepository lotRepository, FifoCalculator fifoCalculator) {
      this.operationRepository = operationRepository; 
      this.lotRepository = lotRepository;
      this.fifoCalculator = fifoCalculator;
   }

   // Listado general de operaciones
   public List<Operation> findAll () {
      return operationRepository.findAll();
   }

   // Posiciones del mismo ticker
   public Position getPosition(String ticker) {
      BigDecimal total = BigDecimal.ZERO;

      for (Operation operation : operationRepository.findByTicker(ticker.toUpperCase())) {
         if(operation.getType() == OperationType.BUY) {
            total = total.add(operation.getQuantity());
         } else {
            total = total.subtract(operation.getQuantity());
         }
      }
      return new Position(ticker.toUpperCase(), total);
   }

   // Posiciones invertidas del mismo ticker
   public Invested getInvested(String ticker) {
      BigDecimal total = BigDecimal.ZERO;

      for (Operation operation : operationRepository.findByTicker(ticker.toUpperCase())) {
         if(operation.getType() == OperationType.BUY) {
            total = total.add(operation.getPrice().multiply(operation.getQuantity()));
         } 
      }
      return new Invested(ticker.toUpperCase(), total);
   }

   // Conteo de las operaciones del mismo ticker
   public Count getCount(String ticker) {
      int count = operationRepository.findByTicker(ticker.toUpperCase()).size();
      return new Count(ticker.toUpperCase(), count);
   }

   // Creacion de operacion y validacion en caso de venta
   private void validateSale(OperationRequest request) {
      Position position = getPosition(request.ticker());
      if(request.quantity().compareTo(position.quantity()) > 0) {
         throw new InsufficientPositionException("No puedes vender " + request.quantity() + " " + request.ticker().toUpperCase() + ": solo tienes " + position.quantity());
      }
   }

   private void registerBuy(OperationRequest request) {
      Lot lot = new Lot(request.ticker().toUpperCase(), request.date(), request.price(), request.quantity());
      lotRepository.save(lot);
   }

   private void registerSale(OperationRequest request) {
      List<Lot> lots = lotRepository.findByTickerOrderByDateAsc(request.ticker().toUpperCase());
      fifoCalculator.sell(lots, request.quantity(), request.price());
      lotRepository.saveAll(lots);
   }
   
   @Transactional 
   public Operation create(OperationRequest request) {
      if(request.type() == OperationType.SELL) {
         validateSale(request);
      }
      
      Operation operation = new Operation(
         request.ticker().toUpperCase(),
         request.type(),
         request.quantity(),
         request.price(),
         request.date()
      );
      Operation saved = operationRepository.save(operation);

      if(request.type() == OperationType.BUY) {
         registerBuy(request);
      } else {
         registerSale(request);
      }

      return saved;
   }
}
