package com.alvaro.tracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alvaro.tracker.model.Lot;

public interface LotRepository extends JpaRepository<Lot, Long> {
   List<Lot> findByTickerOrderByDateAsc(String ticker);
}
