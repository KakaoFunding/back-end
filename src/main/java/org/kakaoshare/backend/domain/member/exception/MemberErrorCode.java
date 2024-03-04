package org.kakaoshare.backend.domain.member.exception;

public enum MemberErrorCode {
    NOT_FOUND("일치하는 회원 정보가 없습니다.");

    private final String message;

    MemberErrorCode(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
