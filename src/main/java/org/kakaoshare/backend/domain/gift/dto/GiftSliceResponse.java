package org.kakaoshare.backend.domain.gift.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.kakaoshare.backend.common.dto.PageResponse;
import org.kakaoshare.backend.domain.gift.entity.Gift;
import org.springframework.data.domain.Page;

@Getter
@Builder
public class GiftSliceResponse {
    private final PageResponse<GiftResponse> pageResponse;

    public static GiftSliceResponse from(Page<Gift> giftPage) {
        Page<GiftResponse> giftResponsePage = giftPage.map(GiftResponse::from);

        PageResponse<GiftResponse> pageResponse = (PageResponse<GiftResponse>) PageResponse.from(giftResponsePage);

        return GiftSliceResponse.builder()
                .pageResponse(pageResponse)
                .build();
    }
}
