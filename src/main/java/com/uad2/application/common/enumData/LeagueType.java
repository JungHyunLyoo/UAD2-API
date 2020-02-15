package com.uad2.application.common.enumData;

/*
 * @USER Jongyeob Kim
 * @DATE 2020-01-17
 */
public enum LeagueType {

    EPL("eng.1"),
    LALIGA("esp.1"),
    BUNDESLIGA("ger.1"),
    SERIE("ita.1");

    private String league;

    LeagueType(String league) {
        this.league = league;
    }

    public String getLeague() {
        return this.league;
    }

}
