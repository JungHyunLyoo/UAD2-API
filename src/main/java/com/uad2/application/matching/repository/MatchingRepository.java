package com.uad2.application.matching.repository;

import com.uad2.application.matching.entity.Matching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface MatchingRepository extends JpaRepository<Matching,Long> {
    Page<Matching> findAll(Pageable pageable);
    Matching findByMatchingDate(Date date);
    Matching findBySeq(int seq);
}
