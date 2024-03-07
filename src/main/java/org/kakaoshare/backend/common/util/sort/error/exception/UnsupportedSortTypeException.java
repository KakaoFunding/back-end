package org.kakaoshare.backend.common.util.sort.error.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.common.util.sort.error.SortErrorCode;

@Getter
@RequiredArgsConstructor
public class UnsupportedSortTypeException extends RuntimeException{
    private final SortErrorCode errorCode;
}
