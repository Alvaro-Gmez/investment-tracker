package com.alvaro.tracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alvaro.tracker.model.Operation;

public interface OperationRepository extends JpaRepository<Operation, Long> {
   
   List<Operation> findByTicker(String ticker);
}
