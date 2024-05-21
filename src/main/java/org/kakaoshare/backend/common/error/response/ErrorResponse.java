package org.kakaoshare.backend.common.error.response;

import lombok.Builder;

@Builder
public record ErrorResponse(String message,
                            int code
//                            @JsonInclude(JsonInclude.Include.NON_EMPTY)
//                            List<ValidationError> errors
) {
//    @Builder
//    public record ValidationError(String field, String message) {
//
//        public static ValidationError of(final FieldError fieldError) {
//            return ValidationError.builder()
//                    .field(fieldError.getField())
//                    .message(fieldError.getDefaultMessage())
//                    .build();
//        }
//    }
}