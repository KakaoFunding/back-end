package org.kakaoshare.backend.common.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PageResponse<E> {
    private final boolean hasNext;
    private final List<E> items;
    private final int pageNumber;
    private final int pageSize;
    private final int totalPages;
    private final long totalElements;
    private final boolean isLast;

    public static PageResponse<?> from(final Page<?> page) {
        return new PageResponse<>(page.hasNext(), page.getContent(), page.getNumber(), page.getSize(), page.getTotalPages(), page.getTotalElements(), page.isLast());
    }
}
