package com.uad2.application.attendance.entity;

import javax.persistence.*;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class Attendance {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int seq;

    @Column(name = "member_seq", nullable = false)
    private int memberSeq;

    @Column(name = "available_time", length = 40)
    private String availableTime;

    @Column(name = "available_date")
    private Date availableDate;

    @CreationTimestamp
    @Column(nullable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(nullable = false, name = "updated_at")
    private Date updatedAt;


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
        return "attendance{" +
                "seq=" + seq +
                ", memberSeq=" + memberSeq +
                ", availableTime='" + availableTime + '\'' +
                ", availableDate=" + availableDate +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
