package com.alvaro.tracker.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity 
@Table (name = "operations")
public class Operation {

   @Id 
   @GeneratedValue (strategy = GenerationType.IDENTITY)
   private Long id;

   @Column (nullable = false)
   private String ticker;
   
   @Enumerated (EnumType.STRING)
   @Column (nullable = false)
   private OperationType type;

   @Column(nullable = false, precision = 19, scale = 6)
   private BigDecimal quantity;

   @Column(nullable = false, precision = 19, scale = 4)
   private BigDecimal price;

   @Column(nullable = false)
   private LocalDate date;

   protected Operation(){

   }

   public Operation(String ticker, OperationType type, BigDecimal quantity, BigDecimal price, LocalDate date) {
      this.ticker = ticker;
      this.type = type;
      this.quantity = quantity;
      this.price = price;
      this.date = date;
   }

   public Long getId() {
      return id;
   }

   public String getTicker() {
      return ticker;
   }

   public OperationType getType() {
      return type;
   }

   public BigDecimal getQuantity() {
      return quantity;
   }

   public BigDecimal getPrice() {
      return price;
   }

   public LocalDate getDate() {
      return date;
   }

   
}
