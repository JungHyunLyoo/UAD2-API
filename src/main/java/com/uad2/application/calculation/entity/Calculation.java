package com.uad2.application.calculation.entity;

import com.uad2.application.matching.entity.Matching;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;


@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="seq")
public class Calculation {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int seq;

    @OneToOne(targetEntity = Matching.class)
    @JoinColumn(name="matching_seq")
    private Matching matching;

    @Column(nullable = false)
    private int price;

    @Column(length = 100)
    private String content;

    @Column(nullable = false)
    private int kind;

    @Column(name = "calculation_date")
    private Date calculationDate;

    @Column(name = "attend_cnt", nullable = false)
    private int attendCnt;


    @CreationTimestamp
    @Column(nullable = false, name = "create_at", updatable=false)
    private LocalDateTime createAt;

    @UpdateTimestamp
    @Column(nullable = false, name = "update_at")
    private LocalDateTime updateAt;

    public void setMatchingSeq(Matching matching){
        this.matching = matching;
    }
}