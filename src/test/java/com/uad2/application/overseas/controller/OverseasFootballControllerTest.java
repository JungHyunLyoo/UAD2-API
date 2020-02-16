package com.uad2.application.overseas.controller;

import com.uad2.application.BaseControllerTest;
import com.uad2.application.common.TestDescription;
import com.uad2.application.overseas.model.Club;
import com.uad2.application.overseas.model.League;
import com.uad2.application.overseas.model.Match;
import com.uad2.application.overseas.model.Rank;
import com.uad2.application.overseas.service.OverseasFootballService;
import lombok.Getter;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockCookie;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.*;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
 * @USER Jongyeob Kim
 * @DATE 2020-01-20
 */
@AutoConfigureRestDocs(outputDir = "target/generated-snippets/overseas")
public class OverseasFootballControllerTest extends BaseControllerTest {

    @MockBean
    private OverseasFootballService overseasFootballService;

    @Getter
    private enum LEAGUE {
        EPL(0, "English Premier League", "2019-2020"),
        LALIGA(1, "Spanish Primera División", "2019-2020"),
        BUNDES(2, "German Bundesliga", "2019-20 Bundesliga"),
        SERIE(3, "Italian Serie A", "2019/2020");

        private int key;
        private String name;
        private String season;

        LEAGUE(int key, String name, String season) {
            this.key = key;
            this.name = name;
            this.season = season;
        }
    }

    @Getter
    private enum TEAM {
        LIVERPOOL(0, "Liverpool", "https://a.espncdn.com/i/teamlogos/soccer/500/364.png"),
        NORWICH_CITY(1, "Norwich City", "https://a.espncdn.com/i/teamlogos/soccer/500/381.png"),
        BARCELONA(2, "Barcelona", "https://a.espncdn.com/i/teamlogos/soccer/500/83.png"),
        ESPANYOL(3, "Espanyol", "https://a.espncdn.com/i/teamlogos/soccer/500/88.png"),
        RB_LEIPZIG(4, "RB Leipzig", "https://a.espncdn.com/i/teamlogos/soccer/500/11420.png"),
        SC_PADERBORN(5, "SC Paderborn 07", "https://a.espncdn.com/i/teamlogos/soccer/500/3307.png"),
        JUVENTUS(6, "Juventus", "https://a.espncdn.com/i/teamlogos/soccer/500/111.png"),
        SPAL(7, "SPAL", "https://a.espncdn.com/i/teamlogos/soccer/500/3958.png");

        private int key;
        private String name;
        private String logo;

        TEAM(int key, String name, String logo) {
            this.key = key;
            this.name = name;
            this.logo = logo;
        }
    }

    @Test
    @TestDescription("4대 리그(EPL, Laliga, Bundes, SerieA) 리그 정보")
    public void getOverseasFootballInfo() throws Exception {
        MockHttpServletResponse response = super.execLogin("testUser","testUser",false).andReturn().getResponse();

        MockCookie[] cookieList = super.convertCookieToMockCookie(response.getCookies());

        List<Club> clubList = new ArrayList<>();
        for (TEAM el : TEAM.values()) {
            clubList.add(Club.of(el.getName(), el.getLogo()));
        }

        List<List<Rank>> rankLists = Arrays.asList(
                Arrays.asList(
                        Rank.of(1, clubList.get(TEAM.LIVERPOOL.getKey()), 21, 0, 1,
                                22, 52, 14, 64, 38),
                        Rank.of(20, clubList.get(TEAM.NORWICH_CITY.getKey()), 4, 14, 5,
                                23, 23, 45, 17, -22)
                ),
                Arrays.asList(
                        Rank.of(1, clubList.get(TEAM.BARCELONA.getKey()), 13, 3, 4,
                                20, 50, 23, 43, 27),
                        Rank.of(20, clubList.get(TEAM.ESPANYOL.getKey()), 3, 12, 5,
                                20, 16, 37, 14, -21)
                ),
                Arrays.asList(
                        Rank.of(1, clubList.get(TEAM.RB_LEIPZIG.getKey()), 4, 14, 5,
                                23, 23, 45, 17, -22),
                        Rank.of(18, clubList.get(TEAM.SC_PADERBORN.getKey()), 4, 14, 5,
                                23, 23, 45, 17, -22)
                ),
                Arrays.asList(
                        Rank.of(1, clubList.get(TEAM.JUVENTUS.getKey()), 4, 14, 5,
                                23, 23, 45, 17, -22),
                        Rank.of(20, clubList.get(TEAM.SPAL.getKey()), 4, 14, 5,
                                23, 23, 45, 17, -22)
                )
        );

        Date now = new Date();
        List<List<Match>> matchLists = Arrays.asList(
                this.setMatch(now, clubList, TEAM.LIVERPOOL.getKey(), TEAM.NORWICH_CITY.getKey()),
                this.setMatch(now, clubList, TEAM.BARCELONA.getKey(), TEAM.ESPANYOL.getKey()),
                this.setMatch(now, clubList, TEAM.RB_LEIPZIG.getKey(), TEAM.SC_PADERBORN.getKey()),
                this.setMatch(now, clubList, TEAM.JUVENTUS.getKey(), TEAM.SPAL.getKey())
        );

        List<League> leagueList = new ArrayList<>();
        for (LEAGUE el : LEAGUE.values()) {
            leagueList.add(
                    this.setLeague(el.getName(), el.getSeason(), rankLists.get(el.getKey()), matchLists.get(el.getKey()))
            );
        }

        // given
        given(overseasFootballService.getLeagueInfo()).willReturn(leagueList);

        // request
        ResultActions result = mockMvc.perform(
                super.getRequest("/api/overseas/football", cookieList)
        );

        // result
        result.andExpect(
                status().isOk()
        )
                .andDo(print())
                .andExpect(jsonPath("leagueList").isNotEmpty())
                .andDo(document("getAllOverseasFootballLeagues",
                        responseFields(
                                subsectionWithPath("leagueList").description("해외축구 리그(EPL, Laliga, Bundes, Serie) 정보 리스트"),
                                fieldWithPath("leagueList[].name").type(JsonFieldType.STRING).description("리그명"),
                                fieldWithPath("leagueList[].season").type(JsonFieldType.STRING).description("시즌 정보"),
                                fieldWithPath("leagueList[].rankList").type(JsonFieldType.ARRAY).description("팀별 랭킹 정보 리스트"),
                                fieldWithPath("leagueList[].rankList[].rank").type(JsonFieldType.NUMBER).description("랭킹"),
                                fieldWithPath("leagueList[].rankList[].club").type(JsonFieldType.OBJECT).description("클럽 정보"),
                                fieldWithPath("leagueList[].rankList[].club.name").type(JsonFieldType.STRING).description("클럽명"),
                                fieldWithPath("leagueList[].rankList[].club.logo").type(JsonFieldType.STRING).description("로고 url"),
                                fieldWithPath("leagueList[].rankList[].win").type(JsonFieldType.NUMBER).description("승"),
                                fieldWithPath("leagueList[].rankList[].lose").type(JsonFieldType.NUMBER).description("패"),
                                fieldWithPath("leagueList[].rankList[].draw").type(JsonFieldType.NUMBER).description("무"),
                                fieldWithPath("leagueList[].rankList[].gamesPlayed").type(JsonFieldType.NUMBER).description("경기수"),
                                fieldWithPath("leagueList[].rankList[].goals").type(JsonFieldType.NUMBER).description("득점"),
                                fieldWithPath("leagueList[].rankList[].goalsAgainst").type(JsonFieldType.NUMBER).description("실점"),
                                fieldWithPath("leagueList[].rankList[].points").type(JsonFieldType.NUMBER).description("승점"),
                                fieldWithPath("leagueList[].rankList[].goalDifference").type(JsonFieldType.NUMBER).description("득실차"),
                                fieldWithPath("leagueList[].matchList").type(JsonFieldType.ARRAY).description("경기 일정 리스트"),
                                fieldWithPath("leagueList[].matchList[].date").type(JsonFieldType.STRING).description("경기 시간(한국 시간)"),
                                fieldWithPath("leagueList[].matchList[].homeTeam").type(JsonFieldType.OBJECT).description("홈팀 정보"),
                                fieldWithPath("leagueList[].matchList[].homeTeam.name").type(JsonFieldType.STRING).description("홈팀 클럽명"),
                                fieldWithPath("leagueList[].matchList[].homeTeam.logo").type(JsonFieldType.STRING).description("홈팀 로고 url"),
                                fieldWithPath("leagueList[].matchList[].awayTeam").type(JsonFieldType.OBJECT).description("어웨이팀 정보"),
                                fieldWithPath("leagueList[].matchList[].awayTeam.name").type(JsonFieldType.STRING).description("어웨이팀 클럽명"),
                                fieldWithPath("leagueList[].matchList[].awayTeam.logo").type(JsonFieldType.STRING).description("어웨이팀 로고 url")
                        )
                ))
        ;

        verify(overseasFootballService).getLeagueInfo();
    }

    private List<Match> setMatch(Date date, List<Club> clubList, int homeIdx, int awayIdx) {
        return Collections.singletonList(
                Match.of(date, clubList.get(homeIdx), clubList.get(awayIdx))
        );
    }

    private League setLeague(String leagueName, String season, List<Rank> rankList, List<Match> matchList) {
        League league = League.getInstance();
        league.updateLeagueInfo(leagueName, season);
        league.setRankList(rankList);
        league.setMatchList(matchList);

        return league;
    }

}