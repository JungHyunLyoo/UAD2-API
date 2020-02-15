package com.uad2.application.overseas.model;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

/*
 * @USER Jongyeob Kim
 * @DATE 2020-01-17
 */
@Getter
@Builder
public class Match {

    private Date date;

    private Club homeTeam;

    private Club awayTeam;

    public static Match of(Date date, Club homeTeam, Club awayTeam) {
        return Match.builder()
                .date(date)
                .homeTeam(homeTeam)
                .awayTeam(awayTeam)
                .build();
    }

}
