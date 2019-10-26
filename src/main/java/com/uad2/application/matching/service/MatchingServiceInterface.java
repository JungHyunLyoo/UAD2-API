package com.uad2.application.matching.service;/*
 * @USER zkdlwnfm
 * @DATE 2019-10-26
 */

import com.uad2.application.matching.entity.Matching;

import java.util.Date;
import java.util.List;

public interface MatchingServiceInterface {
    Matching createMatching(Matching matching);

    Matching getMatchingByDate(Date date);

    //List<Matching> getAllMatching();

    void updateMatching(Matching matching);
}
