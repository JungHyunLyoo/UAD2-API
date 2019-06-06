package com.uad2.application.repository;

import com.uad2.application.entity.Calculation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalculationRepository extends JpaRepository<Calculation,Long> {}