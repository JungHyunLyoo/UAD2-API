package com.uad2.application.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Member {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int seq;

    @Column(nullable = false, length = 20)
    private String id;

    @Column(nullable = false, length = 100)
    private String pwd;

    @Column(length = 20)
    private String name;

    @Column(name = "phone_number" ,length = 15)
    private String phoneNumber;

    @Column(name = "student_id")
    private int studentId;

    @Column(name = "birth_day")
    private Date birthDay;

    @Column(name = "attd_cnt")
    private int attdCnt;

    @Column(name = "profile_img",length = 30)
    private String profileImg;

    @Column(name = "session_id",length = 30)
    private String sessionId;


    @Column(name = "session_limit")
    private Date sessionLimit;

    @Column(nullable = false, name = "is_admin")
    private boolean isAdmin;

    @Column(name = "is_worker")
    private boolean isWorker;

    @Column(name = "is_benefit")
    private boolean isBenefit;

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

    public String getId() {
        return id;
    }

    public String getPwd() {
        return pwd;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getStudentId() {
        return studentId;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public int getAttdCnt() {
        return attdCnt;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public String getSessionId() {
        return sessionId;
    }

    public Date getSessionLimit() {
        return sessionLimit;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public boolean isWorker() {
        return isWorker;
    }

    public boolean isBenefit() {
        return isBenefit;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {
        return "Member{" +
                "seq=" + seq +
                ", id='" + id + '\'' +
                ", pwd='" + pwd + '\'' +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", studentId=" + studentId +
                ", birthDay=" + birthDay +
                ", attdCnt=" + attdCnt +
                ", profileImg='" + profileImg + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", sessionLimit=" + sessionLimit +
                ", isAdmin=" + isAdmin +
                ", isWorker=" + isWorker +
                ", isBenefit=" + isBenefit +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
