package com.uad2.application.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Matching {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
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


    @Column(nullable = false, name = "created_at")
    private Date createdAt;

    @Column(nullable = false, name = "updated_at")
    private Date updatedAt;


    @PrePersist
    void createdAt() {
        this.createdAt = this.updatedAt = new Date();
    }

    @PreUpdate
    void updatedAt() {
        this.updatedAt = new Date();
    }

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

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {
        return "Matching{" +
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
