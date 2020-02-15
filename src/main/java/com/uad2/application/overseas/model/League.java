package com.uad2.application.overseas.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/*
 * @USER Jongyeob Kim
 * @DATE 2020-01-17
 */
@Getter
@Setter
public class League {

    private String name;

    private String season;

    private List<Rank> rankList;

    private List<Match> matchList;

    public static League getInstance() {
        return new League();
    }

    public void updateLeagueInfo(String name, String season) {
        this.name = name;
        this.season = season;
    }

}
