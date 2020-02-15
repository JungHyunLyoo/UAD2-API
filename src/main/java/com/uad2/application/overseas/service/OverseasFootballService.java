package com.uad2.application.overseas.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uad2.application.common.enumData.LeagueType;
import com.uad2.application.overseas.model.Club;
import com.uad2.application.overseas.model.League;
import com.uad2.application.overseas.model.Match;
import com.uad2.application.overseas.model.Rank;
import com.uad2.application.utils.HttpRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/*
 * @USER Jongyeob Kim
 * @DATE 2020-01-17
 */
@Service
public class OverseasFootballService {

    private final String ESPN_URL = "http://site.api.espn.com";

    private final Logger logger = LoggerFactory.getLogger(OverseasFootballService.class);

    private final ObjectMapper mapper;

    public OverseasFootballService(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public List<League> getLeagueInfo() {
        List<League> leagueList = new ArrayList<>();
        for (LeagueType leagueType : LeagueType.values()) {
            League league = League.getInstance();
            setRankInfo(league, leagueType);
            league.setMatchList(this.getMathchInfo(leagueType));
            leagueList.add(league);
        }

        return leagueList;
    }

    private void setRankInfo(League league, LeagueType leagueType) {
        String url = ESPN_URL + "/apis/v2/sports/soccer/" + leagueType.getLeague() + "/standings";
        List<Rank> rankList = new ArrayList<>();
        try {
            JsonNode jsonNode = mapper.readTree(HttpRequestUtil.getRequest(url));
            league.updateLeagueInfo(
                    jsonNode.get("name").toString().replaceAll("\"", ""),    // 리그명
                    jsonNode.get("children").get(0).get("abbreviation").toString().replaceAll("\"", "")  // 시즌
            );
            JsonNode rankInfo = jsonNode
                    .get("children")
                    .get(0)
                    .get("standings")
                    .get("entries");

            int rank = 0;
            for (JsonNode node : rankInfo) {
                // 클럽명
                String clubName = node.get("team").get("name").toString().replaceAll("\"", "");
                // 로고
                String logo = node.get("team").get("logos").get(0).get("href").toString().replaceAll("\"", "");
                // 승
                int win = this.getValue(node, 0);
                // 패
                int lose = this.getValue(node, 1);
                // 무
                int draw = this.getValue(node, 2);
                // 경기수
                int gamesPlayed = this.getValue(node, 3);
                // 득점
                int goals = this.getValue(node, 4);
                // 실점
                int goalsAgainst = this.getValue(node, 5);
                // 승점
                int points = this.getValue(node, 6);
                // 득실차
                int goalDifference = this.getValue(node, 8);

                rankList.add(
                        Rank.of(
                                ++rank, Club.of(clubName, logo), win, lose, draw,
                                gamesPlayed, goals, goalsAgainst, points, goalDifference
                        )
                );
            }
        } catch (IOException e) {
            logger.error("ESPN API Error ! ", e);
            throw new RuntimeException("ESPN API error");
        }

        league.setRankList(rankList);
    }

    private List<Match> getMathchInfo(LeagueType leagueType) {
        String url = ESPN_URL + "/apis/site/v2/sports/soccer/" + leagueType.getLeague() + "/scoreboard";
        List<Match> gameList = new ArrayList<>();
        try {
            JsonNode gameInfo = mapper.readTree(HttpRequestUtil.getRequest(url))
                    .get("events");

            for (JsonNode node : gameInfo) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                Calendar matchDate = Calendar.getInstance();
                matchDate.setTime(dateFormat.parse(node.get("date").toString().substring(1, 18)));
                matchDate.add(Calendar.HOUR_OF_DAY, 9); // 한국 시간 : 영국 현지 시간 + 9시간

                String home = this.getNameOrLogo(node, 0, "name");
                String homeLogo = this.getNameOrLogo(node, 0, "logo");
                String away = this.getNameOrLogo(node, 1, "name");
                String awayLogo = this.getNameOrLogo(node, 1, "logo");
                Club homeTeam = Club.of(home, homeLogo);
                Club awayTeam = Club.of(away, awayLogo);

                gameList.add(Match.of(new Date(matchDate.getTimeInMillis()), homeTeam, awayTeam));
            }
        } catch (IOException | ParseException e) {
            logger.error("ESPN API Error ! ", e);
            throw new RuntimeException("ESPN API error");
        }

        return gameList;
    }

    private int getValue(JsonNode node, int index) {
        return Integer.parseInt(node.get("stats").get(index).get("displayValue").toString().replaceAll("\"", ""));
    }

    private String getNameOrLogo(JsonNode node, int index, String type) {
        return node.get("competitions")
                .get(0)
                .get("competitors")
                .get(index)
                .get("team")
                .get(type)
                .toString().replaceAll("\"", "");
    }

}
