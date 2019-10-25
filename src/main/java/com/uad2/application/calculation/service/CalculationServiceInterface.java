package com.uad2.application.calculation.service;/*
 * @USER zkdlwnfm
 * @DATE 2019-10-26
 */

import com.uad2.application.calculation.entity.Calculation;

import java.util.Date;

public interface CalculationServiceInterface {
    Calculation getCalculationByCalculationDate(Date date);

    void updateCalculation(Calculation calculation);
}
