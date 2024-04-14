package org.kakaoshare.backend.domain.order.dto.preview;

import org.kakaoshare.backend.domain.option.dto.OptionSummaryRequest;

import java.util.List;

public record OrderPreviewRequest(Long productId, Integer quantity, List<OptionSummaryRequest> options) {
}
