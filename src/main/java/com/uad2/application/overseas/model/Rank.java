package com.uad2.application.overseas.model;

import lombok.Builder;
import lombok.Getter;

/*
 * @USER Jongyeob Kim
 * @DATE 2020-01-17
 */
@Getter
@Builder
public class Rank {

    private int rank;

    private Club club;

    private int win;

    private int lose;

    private int draw;

    private int gamesPlayed;

    private int goals;

    private int goalsAgainst;

    private int points;

    private int goalDifference;

    public static Rank of(
            int rank, Club club, int win, int lose, int draw,
            int gamesPlayed, int goals, int goalsAgainst, int points, int goalDifference) {
        return Rank.builder()
                .rank(rank)
                .club(club)
                .win(win)
                .lose(lose)
                .draw(draw)
                .gamesPlayed(gamesPlayed)
                .goals(goals)
                .goalsAgainst(goalsAgainst)
                .points(points)
                .goalDifference(goalDifference)
                .build();
    }

}
