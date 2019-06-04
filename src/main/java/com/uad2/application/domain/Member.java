package com.uad2.application.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Member implements Serializable {
    @Id
    @Column(name = "seq")
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

    @Create
    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @PrePersist
    void createdAt() {
        this.createdAt = this.updatedAt = new Date();
    }

    @PreUpdate
    void updatedAt() {
        this.updatedAt = new Date();
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
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    /*
        private boolean isAdmin;
        private boolean isWorker;
        private boolean isBenefit;
    */
}
