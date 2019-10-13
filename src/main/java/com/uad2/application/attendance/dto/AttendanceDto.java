package com.uad2.application.attendance.dto;

/*
 * @USER Jongyeob
 * @DATE 2019-09-19
 * @DESCRIPTION 회원 도메인에 대한 request, response 처리하는 커맨드 객체
 */

import com.uad2.application.member.dto.MemberDto;
import com.uad2.application.member.entity.Member;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.Date;

public class AttendanceDto {

    private static ModelMapper modelMapper = new ModelMapper();

    @Getter
    @Setter
    public static class Request {   // 프로퍼티 공통 사용여부에 따라 Create, Update로 분리해도 괜찮을 것 같다.
        private int memberSeq;
        private String availableTime;
        private Date availableDate;
    }

    @Getter
    @Setter
    public static class Response {
        private MemberDto.Response member;
        private String availableTime;
        private Date availableDate;

        public void setMember(Member member){
            this.member = modelMapper.map(member, MemberDto.Response.class);
        }
    }
}
