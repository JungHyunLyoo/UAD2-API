package com.uad2.application.overseas.model;

import lombok.Builder;
import lombok.Getter;

/*
 * @USER Jongyeob Kim
 * @DATE 2020-01-17
 */
@Getter
@Builder
public class Club {

    String name;

    String logo;

    public static Club of(String name, String logo) {
        return Club.builder()
                .name(name)
                .logo(logo)
                .build();
    }

}
