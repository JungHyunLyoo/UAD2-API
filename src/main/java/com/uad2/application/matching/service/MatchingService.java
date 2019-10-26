package com.uad2.application.matching.service;
/*
 * @USER zkdlwnfm
 * @DATE 2019-10-20
 */

import com.uad2.application.matching.entity.Matching;
import com.uad2.application.matching.repository.MatchingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MatchingService {
    @Autowired
    private MatchingRepository matchingRepository;

    /*public Matching createMatching(Matching matching) {
        return matchingRepository.save(matching);
    }*/

    public Matching getMatchingByDate(Date date) {
        return matchingRepository.findByMatchingDate(date);
    }

    public Matching updateMatching(Matching matching) {
        return matchingRepository.save(matching);
    }
}
