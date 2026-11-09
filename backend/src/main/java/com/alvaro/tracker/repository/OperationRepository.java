package com.alvaro.tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alvaro.tracker.model.Operation;

public interface OperationRepository extends JpaRepository<Operation, Long> {
}
