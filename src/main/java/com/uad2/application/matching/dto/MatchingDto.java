package com.uad2.application.matching.dto;/*
 * @USER zkdlwnfm
 * @DATE 2019-10-13
 */

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

public class MatchingDto {

    @Getter
    @Setter
    @Builder
    public static class Request {
        private int seq;

        private String matchingDate;

        private String matchingTime;

        private String matchingPlace;

        private String content;

        private String attendMember;

        private String maxCnt;

        private int price;
    }

    @Getter
    @Setter
    public static class Response {
        private String matchingDate;

        private String matchingTime;

        private String matchingPlace;

        private String content;

        private String attendMember;

        private String maxCnt;

        private int price;
    }

}
