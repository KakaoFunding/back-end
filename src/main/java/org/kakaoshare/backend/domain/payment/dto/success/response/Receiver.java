package org.kakaoshare.backend.domain.payment.dto.success.response;

public record Receiver(String name, String photoUrl) {
    public static Receiver empty() {
        return new Receiver("이름 없음", null);
    }
}
