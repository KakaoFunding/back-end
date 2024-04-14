package org.kakaoshare.backend.domain.wish.repository.query;

import org.kakaoshare.backend.domain.wish.dto.WishDetail;

import java.util.List;

public interface WishRepositoryCustom {
    List<WishDetail> findWishDetailsByProviderId(final String providerId);
}
