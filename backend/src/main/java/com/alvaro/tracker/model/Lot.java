package com.alvaro.tracker.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "lots")
public class Lot {

   @Id 
   @GeneratedValue (strategy = GenerationType.IDENTITY)
   private Long id;

   @Column (nullable = false)
   private String ticker;
   
   @Column (nullable = false)
   private LocalDate date;

   @Column(nullable = false, precision = 19, scale = 4)
   private BigDecimal price;

   @Column(nullable = false, precision = 19, scale = 6)
   private BigDecimal initialQuantity;

   @Column(nullable = false, precision = 19, scale = 6)
   private BigDecimal remainingQuantity;

   protected Lot() {

   }

   public Lot(String ticker, LocalDate date, BigDecimal price, BigDecimal initialQuantity) {
      this.ticker = ticker;
      this.date = date;
      this.price = price;
      this.initialQuantity = initialQuantity;
      this.remainingQuantity = initialQuantity;
   }

   public Long getId() {
      return id;
   }

   public String getTicker() {
      return ticker;
   }

   public LocalDate getDate() {
      return date;
   }

   public BigDecimal getPrice() {
      return price;
   }

   public BigDecimal getInitialQuantity() {
      return initialQuantity;
   }

   public BigDecimal getRemainingQuantity() {
      return remainingQuantity;
   }

   public void consume (BigDecimal quantity) {
      if(quantity.compareTo(this.remainingQuantity) > 0)
         throw new IllegalArgumentException("No puedes vender " + quantity + " " + ticker.toUpperCase() + ". Solo tienes " + this.remainingQuantity);
      
      this.remainingQuantity = this.remainingQuantity.subtract(quantity);
   }
}
