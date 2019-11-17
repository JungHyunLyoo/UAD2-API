package com.uad2.application.matching.repository;

import com.uad2.application.matching.entity.Matching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MatchingRepository extends JpaRepository<Matching,Long> {
    Page<Matching> findAll(Pageable pageable);

    @Query(value="SELECT * FROM Matching WHERE matching_date like ?1%",nativeQuery = true)
    List<Matching> findByMatchingDate(String date);

    Matching findBySeq(int seq);
}
