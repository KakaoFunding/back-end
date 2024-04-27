package org.kakaoshare.backend.domain.wish.dto;
public record WishDetail(Long wishId,Long productId, String productName, Long productPrice, String productPhoto, boolean isPublic) {
}