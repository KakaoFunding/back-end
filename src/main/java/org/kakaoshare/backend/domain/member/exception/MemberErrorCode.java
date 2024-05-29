package org.kakaoshare.backend.domain.member.exception;

import lombok.Getter;
import org.kakaoshare.backend.common.error.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
public enum MemberErrorCode implements ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "일치하는 회원 정보가 없습니다."),
    NO_SUCH_RELATIONSHIP(HttpStatus.INTERNAL_SERVER_ERROR, "친구가 아닙니다(사용자간 관계가 부적절합니다).");;

    private final HttpStatus httpStatus;
    private final String message;

    MemberErrorCode(final HttpStatus httpStatus, final String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public String getCode() {
        return "";
    }
}
