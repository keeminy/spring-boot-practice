package com.example.demo.common.codes;

import lombok.Getter;

/**
 * https://adjh54.tistory.com/79 [Contributor9:티스토리]
 */
@Getter
public enum SuccessCode {

    SELECT_SUCCESS(200, "200", "SELECT SUCCESS"),

    DELETE_SUCCESS(200, "200", "DELETE SUCCESS"),

    INSERT_SUCCESS(201, "201", "INSERT SUCCESS"),

    UPDATE_SUCCESS(204, "204", "UPDATE SUCCESS")

    ;

    private final int status;

    private final String code;

    private final String message;

    // 생성자 구성
    SuccessCode(final int status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
