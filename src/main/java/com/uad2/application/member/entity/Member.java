package com.uad2.application.member.entity;


import com.uad2.application.utils.EncryptUtil;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter @Setter
@ToString
@Builder
@AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode(of="id")
public class Member {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int seq;

    @Column(nullable = false, length = 20, unique = true)
    private String id;

    @Column(nullable = false, length = 100)
    private String pwd;

    @Column(length = 20)
    private String name;

    @Column(name = "phone_number" ,length = 15, unique = true)
    private String phoneNumber;

    @Column(name = "student_id")
    private int studentId;

    @Column(name = "birth_day")
    private Date birthDay;

    @Column(name = "attd_cnt", columnDefinition = "int default 0")
    private int attdCnt;

    @Column(name = "profile_img",length = 30)
    private String profileImg;

    @Column(name = "session_id",length = 30)
    private String sessionId;


    @Column(name = "session_limit")
    private Date sessionLimit;

    @Column(name = "is_admin", columnDefinition = "int default 0")
    private int isAdmin;

    @Column(name = "is_worker", columnDefinition = "int default 0")
    private int isWorker;

    @Column(name = "is_benefit", columnDefinition = "int default 0")
    private int isBenefit;

    @CreationTimestamp
    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void encryptPassword(){
        this.pwd = EncryptUtil.encryptMD5(pwd);
    }
}
