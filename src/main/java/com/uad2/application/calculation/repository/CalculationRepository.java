package com.uad2.application.calculation.repository;

import com.uad2.application.calculation.entity.Calculation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalculationRepository extends JpaRepository<Calculation,Long> {}