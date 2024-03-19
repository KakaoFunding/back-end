package org.kakaoshare.backend.common.dto;

import lombok.Getter;
import org.springframework.data.domain.Slice;

import java.util.Collections;
import java.util.List;

@Getter
public class PageResponse<E> {
    private boolean hasNext;
    private List<E> items;

    private PageResponse() {

    }

    private PageResponse(final boolean hasNext, final List<E> items) {
        this.hasNext = hasNext;
        this.items = items;
    }

    public static PageResponse<?> of(final boolean hasNext, final List<?> items) {
        return new PageResponse<>(hasNext, items);
    }

    public static PageResponse<?> from(final Slice<?> slice) {
        return new PageResponse<>(slice.hasNext(), slice.getContent());
    }

    public static PageResponse<?> empty() {
        return new PageResponse<>(false, Collections.emptyList());
    }
}
