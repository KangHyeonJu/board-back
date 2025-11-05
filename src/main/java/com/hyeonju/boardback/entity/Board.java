package com.hyeonju.boardback.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Entity
@Setter @Getter
@NoArgsConstructor
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ColumnDefault("0")
    private Long viewCnt = 0L;

    @Column(columnDefinition = "boolean default false")
    private Boolean isDelete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private LocalDate regDate;

    private LocalDate modDate;

    public Board(String title, String content, Member member, LocalDate regDate) {
        this.title = title;
        this.content = content;
        this.viewCnt = 0L;
        this.member = member;
        this.regDate = regDate;
    }
}
