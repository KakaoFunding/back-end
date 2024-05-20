package org.kakaoshare.backend.domain.cart.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kakaoshare.backend.domain.cart.entity.Cart;
import org.kakaoshare.backend.domain.cart.repository.CartRepository;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.member.repository.MemberRepository;
import org.kakaoshare.backend.domain.option.entity.Option;
import org.kakaoshare.backend.domain.option.entity.OptionDetail;
import org.kakaoshare.backend.domain.option.repository.OptionDetailRepository;
import org.kakaoshare.backend.domain.option.repository.OptionRepository;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.kakaoshare.backend.domain.product.repository.ProductRepository;
import org.kakaoshare.backend.fixture.MemberFixture;
import org.kakaoshare.backend.fixture.ProductFixture;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {
    @Mock
    private CartRepository cartRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private OptionRepository optionRepository;
    @Mock
    private OptionDetailRepository optionDetailRepository;

    @InjectMocks
    private CartService cartService;

}
