package org.kakaoshare.backend.domain.gift.controller;

import org.kakaoshare.backend.common.util.ParamEnum;
import org.kakaoshare.backend.domain.gift.entity.GiftStatus;

import java.util.Arrays;
import java.util.List;

public enum GiftStatusConstraint implements ParamEnum {
    USABLE_CONSTRAINT("USABLE", List.of(GiftStatus.USING, GiftStatus.NOT_USED)),
    USED_CONSTRAINT("USED", List.of(GiftStatus.CANCEL_REFUND, GiftStatus.USED));

    private final String param;
    private final List<GiftStatus> statuses;

    GiftStatusConstraint(final String param, final List<GiftStatus> statuses) {
        this.param = param;
        this.statuses = statuses;
    }

    public static List<GiftStatus> findByParam(final String param) {
        return Arrays.stream(values())
                .filter(status -> status.hasParam(param))
                .findAny()
                .map(giftStatusConstraint -> giftStatusConstraint.statuses)
                .orElseThrow(() -> new IllegalArgumentException()); // TODO 5/29 예외 핸들링을 어떻게 할지 고민
    }

    private boolean hasParam(final String param) {
        return this.param.equals(param);
    }

    @Override
    public String getParamName() {
        return param;
    }
}
