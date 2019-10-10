package com.uad2.application.member.dto;

/*
 * @USER Jongyeob
 * @DATE 2019-09-19
 * @DESCRIPTION 회원 도메인에 대한 request, response 처리하는 커맨드 객체
 */

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class MemberDto {

    @Getter
    @Setter
    @Builder
    public static class Request {   // 프로퍼티 공통 사용여부에 따라 Create, Update로 분리해도 괜찮을 것 같다.
        private String id;

        private String pwd;

        private String name;

        private Date birthDay;

        private int studentId;

        private int isWorker;

        private String phoneNumber;

        private Boolean isAutoLogin;
    }

    @Getter
    @Setter
    public static class EditRequest {
        private String pwd;

        private Date birthDay;

        private int studentId;

        private String phoneNumber;
    }

    @Getter
    @Setter
    public static class Response {
        private String id;

        private String name;

        private int seq;

        private String profileImg;

        private int attdCnt;

        private Date birthDay;

        private int studentId;

        private int isWorker;

        private String phoneNumber;

        private String sessionId;

        private Date sessionLimit;

        private int isAdmin;

        private int isBenefit;
    }

}
