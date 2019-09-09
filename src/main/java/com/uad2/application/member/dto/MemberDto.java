package com.uad2.application.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/*
 * @USER JungHyun
 * @DATE 2019-09-09
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    @NotEmpty
    private String id;
    private String pwd;
}
