package org.kakaoshare.backend.domain.gift.dto.funding.inquiry.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kakaoshare.backend.common.util.EnumValue;
import org.kakaoshare.backend.domain.gift.entity.GiftStatus;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class FundingGiftHistoryRequest {
    @EnumValue(enumClass = GiftStatus.class, ignoreCase = true, message = "선물 상태 값이 잘못되었습니다.", nullable = true)
    private String status;
}
