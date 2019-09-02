package com.uad2.application.calculation;

import com.uad2.application.calculation.Calculation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalculationRepository extends JpaRepository<Calculation,Long> {}