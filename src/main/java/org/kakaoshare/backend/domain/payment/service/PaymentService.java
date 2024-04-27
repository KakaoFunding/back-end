package org.kakaoshare.backend.domain.payment.service;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.common.util.RedisUtils;
import org.kakaoshare.backend.domain.funding.entity.Funding;
import org.kakaoshare.backend.domain.funding.entity.FundingDetail;
import org.kakaoshare.backend.domain.funding.exception.FundingErrorCode;
import org.kakaoshare.backend.domain.funding.exception.FundingException;
import org.kakaoshare.backend.domain.funding.repository.FundingDetailRepository;
import org.kakaoshare.backend.domain.funding.repository.FundingRepository;
import org.kakaoshare.backend.domain.gift.entity.Gift;
import org.kakaoshare.backend.domain.gift.repository.GiftRepository;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.member.exception.MemberException;
import org.kakaoshare.backend.domain.member.repository.MemberRepository;
import org.kakaoshare.backend.domain.option.dto.OptionSummaryResponse;
import org.kakaoshare.backend.domain.option.entity.Option;
import org.kakaoshare.backend.domain.option.repository.OptionDetailRepository;
import org.kakaoshare.backend.domain.option.repository.OptionRepository;
import org.kakaoshare.backend.domain.order.dto.OrderSummaryResponse;
import org.kakaoshare.backend.domain.order.entity.Order;
import org.kakaoshare.backend.domain.order.repository.OrderRepository;
import org.kakaoshare.backend.domain.payment.dto.FundingOrderDetail;
import org.kakaoshare.backend.domain.payment.dto.OrderDetail;
import org.kakaoshare.backend.domain.payment.dto.OrderDetails;
import org.kakaoshare.backend.domain.payment.dto.approve.response.KakaoPayApproveResponse;
import org.kakaoshare.backend.domain.payment.dto.preview.PaymentPreviewRequest;
import org.kakaoshare.backend.domain.payment.dto.preview.PaymentPreviewResponse;
import org.kakaoshare.backend.domain.payment.dto.ready.request.PaymentFundingReadyRequest;
import org.kakaoshare.backend.domain.payment.dto.ready.request.PaymentReadyProductDto;
import org.kakaoshare.backend.domain.payment.dto.ready.request.PaymentReadyRequest;
import org.kakaoshare.backend.domain.payment.dto.ready.response.KakaoPayReadyResponse;
import org.kakaoshare.backend.domain.payment.dto.ready.response.PaymentReadyResponse;
import org.kakaoshare.backend.domain.payment.dto.success.request.PaymentSuccessRequest;
import org.kakaoshare.backend.domain.payment.dto.success.response.PaymentSuccessResponse;
import org.kakaoshare.backend.domain.payment.dto.success.response.Receiver;
import org.kakaoshare.backend.domain.payment.entity.Payment;
import org.kakaoshare.backend.domain.payment.entity.PaymentMethod;
import org.kakaoshare.backend.domain.payment.exception.PaymentException;
import org.kakaoshare.backend.domain.payment.repository.PaymentRepository;
import org.kakaoshare.backend.domain.product.dto.ProductSummaryResponse;
import org.kakaoshare.backend.domain.product.exception.ProductErrorCode;
import org.kakaoshare.backend.domain.product.exception.ProductException;
import org.kakaoshare.backend.domain.product.repository.ProductRepository;
import org.kakaoshare.backend.domain.receipt.entity.Receipt;
import org.kakaoshare.backend.domain.receipt.entity.ReceiptOption;
import org.kakaoshare.backend.domain.receipt.entity.Receipts;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.kakaoshare.backend.domain.member.exception.MemberErrorCode.NOT_FOUND;
import static org.kakaoshare.backend.domain.payment.exception.PaymentErrorCode.INVALID_AMOUNT;
import static org.kakaoshare.backend.domain.payment.exception.PaymentErrorCode.INVALID_OPTION;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PaymentService {
    private final FundingRepository fundingRepository;
    private final FundingDetailRepository fundingDetailRepository;
    private final GiftRepository giftRepository;
    private final MemberRepository memberRepository;
    private final OptionRepository optionRepository;
    private final OptionDetailRepository optionDetailRepository;
    private final OrderRepository orderRepository;
    private final OrderNumberProvider orderNumberProvider;
    private final PaymentRepository paymentRepository;
    private final PaymentWebClientService webClientService;
    private final ProductRepository productRepository;
    private final RedisUtils redisUtils;

    public PaymentPreviewResponse preview(final List<PaymentPreviewRequest> paymentPreviewRequests) {
        final List<String> methodNames = PaymentMethod.getNames();
        final Long totalProductAmount = getTotalProductAmount(paymentPreviewRequests);
        return new PaymentPreviewResponse(0L, methodNames, totalProductAmount);  // TODO: 3/26/24 쇼핑포인트는 0으로 고정
    }

    public PaymentReadyResponse ready(final String providerId,
                                      final List<PaymentReadyRequest> paymentReadyRequests) {
        validateTotalAmount(paymentReadyRequests);
        validateOptionDetailIds(paymentReadyRequests);
        final String orderDetailKey = orderNumberProvider.createOrderDetailKey();
        final OrderDetails orderDetails = getOrderDetails(paymentReadyRequests);
        final List<PaymentReadyProductDto> paymentProductReadyRequests = getPaymentProductReadyRequests(paymentReadyRequests);
        final KakaoPayReadyResponse kakaoPayReadyResponse = webClientService.ready(providerId, paymentProductReadyRequests, orderDetailKey);
        redisUtils.save(orderDetailKey, orderDetails);
        return new PaymentReadyResponse(kakaoPayReadyResponse.tid(), kakaoPayReadyResponse.next_redirect_pc_url(), orderDetailKey);
    }

    public PaymentReadyResponse readyFunding(final String providerId,
                                             final PaymentFundingReadyRequest paymentFundingReadyRequest) {
        final String orderDetailKey = orderNumberProvider.createOrderDetailKey();
        final FundingOrderDetail fundingOrderDetail = FundingOrderDetail.from(paymentFundingReadyRequest);
        redisUtils.save(orderDetailKey, fundingOrderDetail);
        final Long fundingId = paymentFundingReadyRequest.fundingId();
        final Funding funding = findFundingById(fundingId);
        final String name = funding.getProduct().getName();
        final PaymentReadyProductDto paymentReadyProductDto = new PaymentReadyProductDto(name, 1, paymentFundingReadyRequest.amount());// TODO: 4/20/24 펀딩 결제는 단일 상품이므로 수량은 1개
        final KakaoPayReadyResponse kakaoPayReadyResponse = webClientService.ready(providerId, List.of(paymentReadyProductDto), orderDetailKey);
        return new PaymentReadyResponse(kakaoPayReadyResponse.tid(), kakaoPayReadyResponse.next_redirect_pc_url(), orderDetailKey);
    }

    @Transactional
    public PaymentSuccessResponse approve(final String providerId,
                                          final PaymentSuccessRequest paymentSuccessRequest) {
        final KakaoPayApproveResponse approveResponse = webClientService.approve(providerId, paymentSuccessRequest);
        final Payment payment = saveAndGetPayment(approveResponse);
        final Member recipient = findMemberByProviderId(providerId); // TODO: 3/30/24 토큰에 저장된 값이 PK가 아니라 Member 엔티티를 가져와야 함
        final Member receiver = findMemberByProviderId(providerId); // TODO: 3/28/24 친구목록이 구현되지 않아 나에게로 선물만 구현
        final OrderDetails orderDetails = redisUtils.remove(approveResponse.partner_order_id(), OrderDetails.class);
        final Receipts receipts = getReceipts(recipient.getMemberId(), receiver, orderDetails);
        saveGifts(receipts);
        saveOrders(payment, receipts);
        final List<OrderSummaryResponse> orderSummaries = getOrderSummaries(orderDetails);
        return new PaymentSuccessResponse(Receiver.from(receiver), orderSummaries);
    }

    @Transactional
    public PaymentSuccessResponse approveFunding(final String providerId,
                                                 final PaymentSuccessRequest paymentSuccessRequest) {
        final KakaoPayApproveResponse approveResponse = webClientService.approve(providerId, paymentSuccessRequest);
        final Payment payment = approveResponse.toEntity();
        final FundingOrderDetail fundingOrderDetail = redisUtils.remove(approveResponse.partner_order_id(), FundingOrderDetail.class);
        final Funding funding = findFundingById(fundingOrderDetail.fundingId());
        final Member member = findMemberByProviderId(providerId);
        final Long amount = payment.getTotalPrice();
        saveOrReflectFundingDetail(payment, funding, member, amount);
        funding.increaseAccumulateAmount(amount);
        final Receiver receiver = Receiver.from(funding.getMember());
        return new PaymentSuccessResponse(receiver, Collections.emptyList());   // TODO: 4/20/24 펀딩 결제 완료 페이지 디자인이 없어 빈 리스트를 반환
    }

    private long getTotalProductAmount(final List<PaymentPreviewRequest> paymentPreviewRequests) {
        final List<Long> productIds = extractedProductIds(paymentPreviewRequests, PaymentPreviewRequest::productId);
        final Map<Long, Long> priceByIds = productRepository.findAllPriceByIdsGroupById(productIds);
        if (priceByIds == null || priceByIds.isEmpty()) {
            throw new ProductException(ProductErrorCode.NOT_FOUND_PRODUCT_ERROR);
        }

        return paymentPreviewRequests.stream()
                .mapToLong(paymentPreviewRequest -> priceByIds.get(paymentPreviewRequest.productId()) * paymentPreviewRequest.quantity())
                .sum();
    }

    private void validateTotalAmount(final List<PaymentReadyRequest> paymentReadyRequests) {
        final List<Long> productIds = extractedProductIds(paymentReadyRequests, PaymentReadyRequest::productId);
        final Map<Long, Long> priceByIds = productRepository.findAllPriceByIdsGroupById(productIds);
        if (priceByIds.isEmpty()) {
            throw new ProductException(ProductErrorCode.NOT_FOUND_PRODUCT_ERROR);
        }

        final boolean isAllMatch = paymentReadyRequests.stream()
                .anyMatch(paymentReadyRequest -> paymentReadyRequest.quantity() * priceByIds.get(paymentReadyRequest.productId()) == paymentReadyRequest.totalAmount());
        if (!isAllMatch) {
            throw new PaymentException(INVALID_AMOUNT);
        }
    }

    private List<PaymentReadyProductDto> getPaymentProductReadyRequests(final List<PaymentReadyRequest> paymentReadyRequests) {
        final List<Long> productIds = extractedProductIds(paymentReadyRequests, PaymentReadyRequest::productId);
        final Map<Long, String> nameById = productRepository.findAllNameByIdsGroupById(productIds);
        return paymentReadyRequests.stream()
                .map(paymentReadyRequest -> new PaymentReadyProductDto(nameById.get(paymentReadyRequest.productId()), paymentReadyRequest.quantity(), paymentReadyRequest.totalAmount()))
                .toList();
    }

    private void validateOptionDetailIds(final List<PaymentReadyRequest> paymentReadyRequests) {
        final boolean isAllMatch = paymentReadyRequests.stream()
                .anyMatch(this::matchesOptionsWithProduct);
        if (!isAllMatch) {
            throw new PaymentException(INVALID_OPTION);
        }
    }

    private boolean matchesOptionsWithProduct(final PaymentReadyRequest paymentReadyRequest) {
        final Long productId = paymentReadyRequest.productId();
        final List<Long> optionDetailIds = paymentReadyRequest.optionDetailIds();
        if (optionDetailIds == null || optionDetailIds.isEmpty()) {
            return true;
        }

        final List<Option> options = optionRepository.findByOptionDetailIds(optionDetailIds);
        if (options.size() != optionDetailIds.size()) {
            return false;
        }

        final List<Long> productIds = options.stream()
                .map(option -> option.getProduct().getProductId())
                .distinct()
                .toList();
        return productIds.size() == 1 && productIds.get(0).equals(productId);
    }

    private OrderDetails getOrderDetails(final List<PaymentReadyRequest> paymentReadyRequests) {
        final List<OrderDetail> orderDetails = paymentReadyRequests.stream()
                .map(paymentReadyRequest -> paymentReadyRequest.toOrderDetail(orderNumberProvider.createOrderNumber()))
                .toList();
        return new OrderDetails(orderDetails);
    }

    private Payment saveAndGetPayment(final KakaoPayApproveResponse approveResponse) {
        final Payment payment = approveResponse.toEntity();
        return paymentRepository.save(payment);
    }

    private Receipts getReceipts(final Long recipientId,
                                 final Member receiver,
                                 final OrderDetails orderDetails) {
        final Member recipient = memberRepository.getReferenceById(recipientId);
        final List<Receipt> receipts = orderDetails.getValues()
                .stream()
                .map(orderDetail -> new Receipt(
                        orderDetail.orderNumber(),
                        productRepository.getReferenceById(orderDetail.productId()),
                        orderDetail.quantity(),
                        recipient,
                        receiver,
                        getReceiptOptions(orderDetail.optionDetailIds())))
                .toList();
        return new Receipts(receipts);
    }

    private List<ReceiptOption> getReceiptOptions(final List<Long> optionDetailIds) {
        if (optionDetailIds == null || optionDetailIds.isEmpty()) {
            return Collections.emptyList();
        }

        return optionDetailRepository.findAllById(optionDetailIds)
                .stream()
                .map(optionDetail -> new ReceiptOption(optionDetail.getOption().getName(), optionDetail.getName()))
                .toList();
    }

    private void saveOrders(final Payment payment, final Receipts receipts) {
        final List<Order> orders = receipts.toOrders(payment);
        orderRepository.saveAll(orders);
    }

    private void saveGifts(final Receipts receipts) {
        final List<Gift> gifts = receipts.toGifts(LocalDateTime.now().plusDays(180L));    // TODO: 3/29/24 선물 만료기간은 180일로 설정
        giftRepository.saveAll(gifts);
    }

    private List<OrderSummaryResponse> getOrderSummaries(final OrderDetails orderDetails) {
        return orderDetails.getValues()
                .stream()
                .map(this::getOrderSummary)
                .toList();
    }

    private OrderSummaryResponse getOrderSummary(final OrderDetail orderDetail) {
        final ProductSummaryResponse productSummaryResponse = productRepository.findAllProductSummaryById(orderDetail.productId());
        return new OrderSummaryResponse(productSummaryResponse, orderDetail.quantity(), getOptionSummaryResponses(orderDetail.optionDetailIds()));
    }

    private List<OptionSummaryResponse> getOptionSummaryResponses(final List<Long> optionDetailIds) {
        return optionDetailRepository.findAllById(optionDetailIds)
                .stream()
                .map(OptionSummaryResponse::from)
                .toList();
    }

    private <T> List<Long> extractedProductIds(final List<T> values, final Function<T, Long> mapper) {
        return values.stream()
                .map(mapper)
                .toList();
    }

    private Member findMemberByProviderId(final String providerId) {
        return memberRepository.findMemberByProviderId(providerId)
                .orElseThrow(() -> new MemberException(NOT_FOUND));
    }

    private Funding findFundingById(final Long fundingId) {
        return fundingRepository.findById(fundingId)
                .orElseThrow(() -> new FundingException(FundingErrorCode.NOT_FOUND));
    }

    private void saveOrReflectFundingDetail(final Payment payment, final Funding funding, final Member member, final Long amount) {
        fundingDetailRepository.findByFundingAndMember(funding, member)
                .ifPresentOrElse(
                        fundingDetail -> fundingDetail.increaseAmountAndRate(amount),
                        () -> fundingDetailRepository.save(new FundingDetail(member, funding, payment))
                );
    }
}
