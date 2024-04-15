package org.kakaoshare.backend.domain.payment.dto.preview;

import java.util.List;

public record PaymentPreviewResponse(Long shoppingPoint, List<String> methods, Long totalProductAmount) {
}
