package com.uad2.application.member.dto;

/*
 * @USER Jongyeob
 * @DATE 2019-09-19
 * @DESCRIPTION 회원 도메인에 대한 request, response 처리하는 커맨드 객체
 */

import lombok.*;
import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Date;

public class MemberDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        // 프로퍼티 공통 사용여부에 따라 Create, Update로 분리해도 괜찮을 것 같다.
        private String id;

        private String pwd;

        private String name;

        private String birthDay;

        private int studentId;

        private int isWorker;

        private String phoneNumber;

        MultipartFile profileImg;
    }

    @Getter
    @Setter
    @Builder
    @ToString
    public static class LoginRequest {
        private String id;

        private String pwd;

        private boolean isAutoLogin;

        public boolean getIsAutoLogin(){
            return isAutoLogin;
        }
        public void setIsAutoLogin(boolean isAutoLogin){
            this.isAutoLogin = isAutoLogin;
        }
    }

    @Getter
    @Setter
    public static class EditRequest {
        private String pwd;

        private LocalDate birthDay;

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

        private LocalDate birthDay;

        private int studentId;

        private int isWorker;

        private String phoneNumber;

        private String sessionId;

        private Date sessionLimit;

        private int isAdmin;

        private int isBenefit;
    }

}
