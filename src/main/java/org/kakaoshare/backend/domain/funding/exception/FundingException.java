package org.kakaoshare.backend.domain.funding.exception;

public class FundingException extends RuntimeException {
    private final FundingErrorCode errorCode;

    public FundingException(final FundingErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
