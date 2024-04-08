package org.kakaoshare.backend.domain.gift.service;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.gift.repository.GiftRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class GiftService {
    private final GiftRepository giftRepository;
}
