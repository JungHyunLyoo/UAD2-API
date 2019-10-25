package com.uad2.application.calculation.service;/*
 * @USER zkdlwnfm
 * @DATE 2019-10-25
 */

import com.uad2.application.calculation.entity.Calculation;
import com.uad2.application.calculation.repository.CalculationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CalculationService implements CalculationServiceInterface {

    @Autowired
    private CalculationRepository calculationRepository;

    @Override
    public Calculation getCalculationByCalculationDate(Date date) {
        return calculationRepository.findByCalculationDate(date);
    }

    @Override
    public void updateCalculation(Calculation calculation) {
        calculationRepository.save(calculation);
    }
}
