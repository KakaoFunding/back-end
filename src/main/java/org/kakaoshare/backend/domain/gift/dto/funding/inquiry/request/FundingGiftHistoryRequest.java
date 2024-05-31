package org.kakaoshare.backend.domain.gift.dto.funding.inquiry.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kakaoshare.backend.common.util.EnumValue;
import org.kakaoshare.backend.domain.gift.controller.GiftStatusConstraint;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class FundingGiftHistoryRequest {
    @EnumValue(enumClass = GiftStatusConstraint.class, ignoreCase = true, message = "선물 상태 설정이 잘못되었습니다.")
    private String status;
}
