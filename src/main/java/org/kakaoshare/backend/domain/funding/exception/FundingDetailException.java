package org.kakaoshare.backend.domain.funding.exception;

public class FundingDetailException extends RuntimeException {
    private final FundingDetailErrorCode errorCode;

    public FundingDetailException(final FundingDetailErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
