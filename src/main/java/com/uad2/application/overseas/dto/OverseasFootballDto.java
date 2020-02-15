package com.uad2.application.overseas.dto;

import com.uad2.application.overseas.model.League;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/*
 * @USER Jongyeob Kim
 * @DATE 2020-01-17
 */
@Getter
@Builder
public class OverseasFootballDto {

    private List<League> leagueList;

    public static OverseasFootballDto of(List<League> leagueList) {
        return OverseasFootballDto.builder()
                .leagueList(leagueList)
                .build();
    }

}
