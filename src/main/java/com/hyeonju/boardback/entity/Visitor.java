package com.hyeonju.boardback.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter @Setter
public class Visitor {
    @Id
    private Date visitDate;

    @Column(name = "cnt")
    private Long visitCnt;
}
