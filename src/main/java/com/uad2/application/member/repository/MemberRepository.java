package com.uad2.application.member.repository;

import com.uad2.application.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/*
 * @USER JungHyun
 * @DATE 2019-09-22
 * @DESCRIPTION db 쿼리 생성을 담당하는 매개체 클래스
 */
public interface MemberRepository extends JpaRepository<Member,Long> {
    Page<Member> findAll(Pageable pageable);
//    Member findById(String id);
    Member findById(String id);
    Member findByPhoneNumber(String phoneNumber);
}
