package com.project.crux.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    /*
    400 Bad Request
     */

    DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST, "중복된 아이디가 존재합니다"),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "중복된 이메일이 존재합니다"),
    DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST, "중복된 닉네임이 존재합니다"),
    INVALID_AVGSCORE(HttpStatus.BAD_REQUEST, "평균 평점은 0에서 5사이여야 합니다"),
    INVALID_ARTICLEID(HttpStatus.BAD_REQUEST, "ID 값이 올바르지 않습니다"),

    /*
    401 UNAUTHORIZED : 인증되지 않은 사용자
    */
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 틀렸습니다"),
    INVALID_REVIEW_UPDATE(HttpStatus.UNAUTHORIZED,"리뷰 수정 권한이 없습니다"),

    /*
    404 Not Found
     */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저 정보를 찾을 수 없습니다"),
    GYM_NOT_FOUND(HttpStatus.NOT_FOUND,"해당 클라이밍짐 정보를 찾을 수 없습니다"),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND,"해당 리뷰 정보를 찾을 수 없습니다" );






    private final HttpStatus httpStatus;
    private final String errorMessage;

    ErrorCode(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }
}