package com.uad2.application.calculation.repository;

import com.uad2.application.calculation.entity.Calculation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CalculationRepository extends JpaRepository<Calculation,Long> {
    List<Calculation> findCalculationsByCreateAtAfterAndCreateAtBefore(LocalDateTime strDts, LocalDateTime endDts);
}