package com.uad2.application.matching.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Matching {
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

    public String getSeq() {
        return seq;
    }

    public Date getMatchingDate() {
        return matchingDate;
    }

    public String getMatchingTime() {
        return matchingTime;
    }

    public String getMatchingPlace() {
        return matchingPlace;
    }

    public String getContent() {
        return content;
    }

    public String getAttendMember() {
        return attendMember;
    }

    public String getMaxCnt() {
        return maxCnt;
    }

    @Override
    public String toString() {
        return "matching{" +
                "seq='" + seq + '\'' +
                ", matchingDate=" + matchingDate +
                ", matchingTime='" + matchingTime + '\'' +
                ", matchingPlace='" + matchingPlace + '\'' +
                ", content='" + content + '\'' +
                ", attendMember='" + attendMember + '\'' +
                ", maxCnt='" + maxCnt + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
