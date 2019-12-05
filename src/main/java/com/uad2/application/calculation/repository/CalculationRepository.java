package com.uad2.application.calculation.repository;

import com.uad2.application.calculation.entity.Calculation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface CalculationRepository extends JpaRepository<Calculation,Long> {
    List<Calculation> findCalculationsByCreateAtAfterAndCreateAtBefore(LocalDateTime strDts, LocalDateTime endDts);

    @Query(value="SELECT * FROM Calculation WHERE calculation_date like ?1%",nativeQuery = true)
    List<Calculation> findByCalculationDate(String date);

}