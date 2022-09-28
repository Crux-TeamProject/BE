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
    INVALID_CREW_NAME(HttpStatus.BAD_REQUEST, "크루명을 입력해주세요"),
    ADMIN_REGISTER_SUBMIT(HttpStatus.BAD_REQUEST, "크루 생성자는 가입신청을 할 수 없습니다."),
    REGISTER_EXIST(HttpStatus.BAD_REQUEST, "이미 가입하셨습니다."),
    ALREADY_LIKED_CREW_EXCEPTION(HttpStatus.BAD_REQUEST, "이미 좋아요한 크루 입니다"),
    ALREADY_UNLIKED_CREW_EXCEPTION(HttpStatus.BAD_REQUEST, "이미 삭제된 좋아요 입니다"),
    IMAGE_REQUIRED(HttpStatus.BAD_REQUEST, "하나 이상의 이미지를 포함해야 합니다"),
    REQUIRED_KEYWORDS(HttpStatus.BAD_REQUEST, "키워드를 입력해주세요"),



    /*
    401 UNAUTHORIZED : 인증되지 않은 사용자
    */
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 틀렸습니다"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다"),

    /*
    403 FORBIDDEN : 권한 없음
     */
    INVALID_REVIEW_UPDATE(HttpStatus.FORBIDDEN,"리뷰 수정 권한이 없습니다"),
    INVALID_REVIEW_DELETE(HttpStatus.FORBIDDEN,"리뷰 삭제 권한이 없습니다"),
    INVALID_NOTICE_UPDATE(HttpStatus.FORBIDDEN,"공지사항 수정 권한이 없습니다"),
    INVALID_NOTICE_DELETE(HttpStatus.FORBIDDEN,"공지사항 삭제 권한이 없습니다"),
    NOT_ADMIN(HttpStatus.FORBIDDEN, "크루장만 가능합니다"),
    NOT_PERMIT(HttpStatus.FORBIDDEN, "크루원이 아닙니다"),
    NOT_ADMIN_OR_PERMIT(HttpStatus.FORBIDDEN, "크루에 가입된 사람이 아닙니다"),
    NO_PERMISSION_EXCEPTION(HttpStatus.FORBIDDEN, "권한이 없습니다"),

    /*
    404 Not Found
     */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저 정보를 찾을 수 없습니다"),
    GYM_NOT_FOUND(HttpStatus.NOT_FOUND,"해당 클라이밍짐 정보를 찾을 수 없습니다"),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND,"해당 리뷰 정보를 찾을 수 없습니다" ),
    CREW_NOT_FOUND(HttpStatus.NOT_FOUND,"해당 크루 정보를 찾을 수 없습니다" ),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시글을 찾을 수 없습니다"),
    CREWLEADER_NOT_FOUND(HttpStatus.NOT_FOUND,"해당 크루 리더의 정보를 찾을 수 없습니다" ),
    CREWMEMBER_NOT_FOUND(HttpStatus.NOT_FOUND,"해당 멤버의 크루 가입 정보를 찾을 수 없습니다"),
    SUBSCRIBE_FAIL(HttpStatus.NOT_FOUND,"알림구독을 실패하였습니다"),
    NOTICE_NOT_FOUND(HttpStatus.NOT_FOUND,"공지사항 정보를 찾을 수 없습니다"),
    NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND,"알림 정보를 찾을 수 없습니다");






    private final HttpStatus httpStatus;
    private final String errorMessage;

    ErrorCode(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }
}