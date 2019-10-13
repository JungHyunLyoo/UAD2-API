package com.uad2.application.matching.dto;/*
 * @USER zkdlwnfm
 * @DATE 2019-10-13
 */

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class MatchingDto {

    @Getter
    @Setter
    @Builder
    public static class Request {
        private Date matchingDate;

        private String matchingTime;

        private String matchingPlace;

        private String content;

        private String attendMember;

        private String maxCnt;
    }
}
