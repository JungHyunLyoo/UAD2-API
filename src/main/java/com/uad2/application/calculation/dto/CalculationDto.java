package com.uad2.application.calculation.dto;

/*
 * @USER Jongyeob
 * @DATE 2019-09-19
 * @DESCRIPTION 정산 도메인에 대한 request, response 처리하는 커맨드 객체
 */

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.util.Date;

public class CalculationDto {

    @Getter
    @Setter
    @Builder
    public static class Request {   // 프로퍼티 공통 사용여부에 따라 Create, Update로 분리해도 괜찮을 것 같다.
        private String calculationDate;
        private int price;
        private int matchingSeq;
        private String content;
        private int kind;
    }

}
