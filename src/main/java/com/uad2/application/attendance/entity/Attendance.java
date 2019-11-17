package com.uad2.application.attendance.entity;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDateTime;

import com.uad2.application.member.entity.Member;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter  @Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Attendance {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int seq;

    @ManyToOne(targetEntity = Member.class)
    @JoinColumn(name="member_seq")
    private Member member;

    @Column(name = "available_time", length = 40)
    private String availableTime;

    @Column(name = "available_date")
    private Date availableDate;

    @CreationTimestamp
    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;
}
