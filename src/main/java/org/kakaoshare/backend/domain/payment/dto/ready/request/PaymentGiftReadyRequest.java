package org.kakaoshare.backend.domain.payment.dto.ready.request;

import java.util.List;

public record PaymentGiftReadyRequest(PaymentGiftReadyReceiver receiver,
                                      List<PaymentGiftReadyItem> items) {
}
