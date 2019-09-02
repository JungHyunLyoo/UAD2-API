package com.uad2.application.member.repository;

import com.uad2.application.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {}
