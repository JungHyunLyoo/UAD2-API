package com.uad2.application.entity;

import com.uad2.application.util.EncryptUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
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

    @PrePersist
    public void encryptPassword(){
        this.pwd = EncryptUtil.getEncMD5(pwd);
    }
}
