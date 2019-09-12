package com.uad2.application.member.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

/*
 * @USER JungHyun
 * @DATE 2019-09-09
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberExternalDto {
    @NotEmpty
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

/*
							seq: 2,
							member_id: "123123",
							name: "0",
							pwd: "123123",
							profile_img: null,
							attd_cnt: 4,
							birth_day: "0000-00-00",
							student_id: null,
							is_worker: 6,
							reg_date: "2018-07-22 14:39:24",
							mod_date: null,
							session_key: "0drkuskr6i1672fvo8833hl9fp",
							session_limit: "2018-08-26 01:31:29"

 */