package org.kakaoshare.backend.domain.search.dto;

import java.util.List;

public record ProductSearchRequest(String keyword, List<String> categories, Integer minPrice, Integer maxPrice) {
}
