package com.uad2.application.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Attendance {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int seq;

    @Column(name = "member_seq", nullable = false)
    private int memberSeq;

    @Column(name = "available_time", length = 40)
    private String availableTime;

    @Column(name = "available_date")
    private Date availableDate;


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

    public int getSeq() {
        return seq;
    }

    public int getMemberSeq() {
        return memberSeq;
    }

    public String getAvailableTime() {
        return availableTime;
    }

    public Date getAvailableDate() {
        return availableDate;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {
        return "Attendance{" +
                "seq=" + seq +
                ", memberSeq=" + memberSeq +
                ", availableTime='" + availableTime + '\'' +
                ", availableDate=" + availableDate +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
