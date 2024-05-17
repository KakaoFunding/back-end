package org.kakaoshare.backend.domain.cart.service;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.cart.repository.CartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CartService {
    private final CartRepository cartRepository;
}
