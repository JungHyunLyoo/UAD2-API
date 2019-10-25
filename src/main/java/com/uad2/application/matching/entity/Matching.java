package com.uad2.application.matching.entity;

import javax.persistence.*;
import java.util.Date;

import com.uad2.application.member.entity.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter @Setter
@ToString
public class Matching {
    @OneToMany(targetEntity = Member.class)
    @JoinColumn(name="member_seq")
    private Member member;

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String seq;

    @Column(nullable = false, name = "matching_date")
    private Date matchingDate;

    @Column(name = "matching_time", length = 30)
    private String matchingTime;

    @Column(name = "matching_place", length = 255)
    private String matchingPlace;

    @Column(length = 255)
    private String content;

    @Column(name = "attend_member", length = 255)
    private String attendMember;

    @Column(name = "max_cnt", length = 255)
    private String maxCnt;

    @CreationTimestamp
    @Column(nullable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(nullable = false, name = "updated_at")
    private Date updatedAt;
}
