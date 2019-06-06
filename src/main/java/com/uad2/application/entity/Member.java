package com.uad2.application.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.time.LocalDateTime;

@Entity
public class Member {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @CreationTimestamp
    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public void setAttdCnt(int attdCnt) {
        this.attdCnt = attdCnt;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setSessionLimit(Date sessionLimit) {
        this.sessionLimit = sessionLimit;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public void setWorker(boolean worker) {
        isWorker = worker;
    }

    public void setBenefit(boolean benefit) {
        isBenefit = benefit;
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
