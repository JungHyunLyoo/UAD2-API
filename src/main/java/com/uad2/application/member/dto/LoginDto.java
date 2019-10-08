package com.uad2.application.member.dto;

/*
 * @USER Jongyeob Kim
 * @DATE 2019-10-04
 */

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
public class LoginDto {

    @Size(max = 20)
    @NotBlank
    private String id;

    @Size(max = 100)
    @NotBlank
    private String pwd;

    @NotNull
    private Boolean isAutoLogin;

}
