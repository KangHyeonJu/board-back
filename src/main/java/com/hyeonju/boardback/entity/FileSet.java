package com.hyeonju.boardback.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class FileSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fileSet_id")
    private Long id;

    private Long maxUploadCnt;

    private Long maxSize;

    private String extension;
}
