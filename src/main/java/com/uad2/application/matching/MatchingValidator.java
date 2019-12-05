package com.uad2.application.matching;
/*
 * @USER JungHyun
 * @DATE 2019-12-05
 * @DESCRIPTION
 */

import com.uad2.application.exception.ClientException;
import com.uad2.application.matching.dto.MatchingDto;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class MatchingValidator {

    public void validateCreateMatching(MatchingDto.Request request) {
        String matchingDate = request.getMatchingDate();
        String matchingPlace = request.getMatchingPlace();
        String content = request.getContent();
        if(Objects.isNull(matchingDate)){
            throw new ClientException("matchingDate is null");
        }
        if(Objects.isNull(matchingPlace)){
            throw new ClientException("matchingPlace is null");
        }
        if(Objects.isNull(content)){
            throw new ClientException("content is null");
        }
    }
}
