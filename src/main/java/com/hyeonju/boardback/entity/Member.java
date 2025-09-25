package com.hyeonju.boardback.entity;

import com.hyeonju.boardback.constant.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Setter
@Getter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String email;

    private String password;

    private String nickname;

    private Date birthday;

    private String phoneNumber;

    private String address;

    private String detailAddress;

    @Enumerated(EnumType.STRING)
    private Role role;
}
