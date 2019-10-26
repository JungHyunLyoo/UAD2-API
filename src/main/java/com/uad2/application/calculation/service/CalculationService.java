package com.uad2.application.calculation.service;/*
 * @USER zkdlwnfm
 * @DATE 2019-10-25
 */

import com.uad2.application.calculation.dto.CalculationDto;
import com.uad2.application.calculation.entity.Calculation;
import com.uad2.application.calculation.repository.CalculationRepository;
import com.uad2.application.matching.entity.Matching;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class CalculationService {

    @Autowired
    private CalculationRepository calculationRepository;

    public Calculation saveCalculation(CalculationDto.Request requestCalculation, Matching matching) {
        Calculation calculation = (new ModelMapper()).map(requestCalculation, Calculation.class);
        calculation.setMatchingSeq(matching);
        return calculationRepository.save(calculation);
    }
}
