package org.kakaoshare.backend.domain.payment.dto.ready.request;

import java.util.List;

public record PaymentGiftReadyRequest(String receiverProviderId,
                                      String socialAccessToken,
                                      List<PaymentGiftReadyItem> items) {
}
