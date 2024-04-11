package org.kakaoshare.backend.domain.wish.dto;

import lombok.Getter;
import org.kakaoshare.backend.domain.wish.entity.Wish;
import org.springframework.context.ApplicationEvent;
@Getter
public class WishEvent extends ApplicationEvent {
    public WishEvent(final Wish wish) {
        super(wish);
    }
}
