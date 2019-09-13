package com.uad2.application.member.entity;
/*
 * @USER JungHyun
 * @DATE 2019-09-12
 * @DESCRIPTION 회원 생성용 dto
 */

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberInsertDto {
    private String id;
    private String pwd;
    private String name;
    private Date birthDay;
    private int studentId;
    private int isWorker;
    private String phoneNumber;
}
