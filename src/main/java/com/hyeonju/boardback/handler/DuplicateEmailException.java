package com.hyeonju.boardback.handler;

import lombok.Getter;

@Getter
public class DuplicateEmailException extends RuntimeException{
    private final String email;

    public DuplicateEmailException(String email) {
        this.email = email;
    }

}