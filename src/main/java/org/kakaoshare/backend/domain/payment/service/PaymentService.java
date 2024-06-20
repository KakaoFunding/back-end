package org.kakaoshare.backend.domain.payment.service;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.common.util.RedisUtils;
import org.kakaoshare.backend.domain.friend.exception.FriendException;
import org.kakaoshare.backend.domain.friend.service.KakaoFriendService;
import org.kakaoshare.backend.domain.funding.entity.Funding;
import org.kakaoshare.backend.domain.funding.entity.FundingDetail;
import org.kakaoshare.backend.domain.funding.exception.FundingDetailErrorCode;
import org.kakaoshare.backend.domain.funding.exception.FundingDetailException;
import org.kakaoshare.backend.domain.funding.exception.FundingErrorCode;
import org.kakaoshare.backend.domain.funding.exception.FundingException;
import org.kakaoshare.backend.domain.funding.repository.FundingDetailRepository;
import org.kakaoshare.backend.domain.funding.repository.FundingRepository;
import org.kakaoshare.backend.domain.gift.entity.FundingGift;
import org.kakaoshare.backend.domain.gift.entity.Gift;
import org.kakaoshare.backend.domain.gift.exception.GiftErrorCode;
import org.kakaoshare.backend.domain.gift.exception.GiftException;
import org.kakaoshare.backend.domain.gift.repository.FundingGiftRepository;
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
import org.kakaoshare.backend.domain.order.exception.OrderErrorCode;
import org.kakaoshare.backend.domain.order.exception.OrderException;
import org.kakaoshare.backend.domain.order.repository.OrderRepository;
import org.kakaoshare.backend.domain.payment.dto.FundingOrderDetail;
import org.kakaoshare.backend.domain.payment.dto.OrderDetail;
import org.kakaoshare.backend.domain.payment.dto.OrderDetails;
import org.kakaoshare.backend.domain.payment.dto.approve.response.KakaoPayApproveResponse;
import org.kakaoshare.backend.domain.payment.dto.cancel.request.PaymentCancelDto;
import org.kakaoshare.backend.domain.payment.dto.cancel.request.PaymentCancelRequest;
import org.kakaoshare.backend.domain.payment.dto.cancel.request.PaymentFundingCancelRequest;
import org.kakaoshare.backend.domain.payment.dto.cancel.request.PaymentFundingDetailCancelRequest;
import org.kakaoshare.backend.domain.payment.dto.preview.PaymentPreviewRequest;
import org.kakaoshare.backend.domain.payment.dto.preview.PaymentPreviewResponse;
import org.kakaoshare.backend.domain.payment.dto.ready.request.PaymentFundingReadyRequest;
import org.kakaoshare.backend.domain.payment.dto.ready.request.PaymentGiftReadyItem;
import org.kakaoshare.backend.domain.payment.dto.ready.request.PaymentGiftReadyReceiver;
import org.kakaoshare.backend.domain.payment.dto.ready.request.PaymentGiftReadyRequest;
import org.kakaoshare.backend.domain.payment.dto.ready.request.PaymentReadyProductDto;
import org.kakaoshare.backend.domain.payment.dto.ready.response.KakaoPayReadyResponse;
import org.kakaoshare.backend.domain.payment.dto.ready.response.PaymentReadyResponse;
import org.kakaoshare.backend.domain.payment.dto.success.request.PaymentSuccessRequest;
import org.kakaoshare.backend.domain.payment.dto.success.response.PaymentFundingSuccessResponse;
import org.kakaoshare.backend.domain.payment.dto.success.response.PaymentGiftSuccessResponse;
import org.kakaoshare.backend.domain.payment.dto.success.response.PaymentSuccessReceiver;
import org.kakaoshare.backend.domain.payment.entity.Payment;
import org.kakaoshare.backend.domain.payment.entity.PaymentMethod;
import org.kakaoshare.backend.domain.payment.exception.PaymentErrorCode;
import org.kakaoshare.backend.domain.payment.exception.PaymentException;
import org.kakaoshare.backend.domain.payment.repository.PaymentRepository;
import org.kakaoshare.backend.domain.product.dto.ProductSummaryResponse;
import org.kakaoshare.backend.domain.product.entity.Product;
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
import java.util.Objects;
import java.util.function.Function;

import static org.kakaoshare.backend.domain.funding.exception.FundingErrorCode.INVALID_ATTRIBUTE_AMOUNT;
import static org.kakaoshare.backend.domain.funding.exception.FundingErrorCode.INVALID_STATUS;
import static org.kakaoshare.backend.domain.member.exception.MemberErrorCode.NOT_FOUND;
import static org.kakaoshare.backend.domain.payment.exception.PaymentErrorCode.ALREADY_REFUND;
import static org.kakaoshare.backend.domain.payment.exception.PaymentErrorCode.INVALID_AMOUNT;
import static org.kakaoshare.backend.domain.payment.exception.PaymentErrorCode.INVALID_OPTION;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PaymentService {
    private final FundingRepository fundingRepository;
    private final FundingDetailRepository fundingDetailRepository;
    private final FundingGiftRepository fundingGiftRepository;
    private final GiftRepository giftRepository;
    private final KakaoFriendService kakaoFriendService;
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
                                      final PaymentGiftReadyRequest paymentGiftReadyRequest) {
        final List<PaymentGiftReadyItem> paymentGiftReadyItems = paymentGiftReadyRequest.items();
        validateReceiver(providerId, paymentGiftReadyRequest.receiver());
        validateTotalAmount(paymentGiftReadyItems);
        validateOptionDetailIds(paymentGiftReadyItems);

        final String orderDetailKey = orderNumberProvider.createOrderDetailKey();
        final List<PaymentReadyProductDto> paymentProductReadyRequests = getPaymentProductReadyRequests(paymentGiftReadyItems);
        final KakaoPayReadyResponse kakaoPayReadyResponse = webClientService.ready(providerId, paymentProductReadyRequests, orderDetailKey);

        final OrderDetails orderDetails = getOrderDetails(paymentGiftReadyRequest);
        redisUtils.save(orderDetailKey, orderDetails);
        return new PaymentReadyResponse(kakaoPayReadyResponse.tid(), kakaoPayReadyResponse.next_redirect_pc_url(), orderDetailKey);
    }

    public PaymentReadyResponse readyFunding(final String providerId,
                                             final PaymentFundingReadyRequest paymentFundingReadyRequest) {
        final int amount = paymentFundingReadyRequest.amount();
        final Long fundingId = paymentFundingReadyRequest.fundingId();
        final Funding funding = findFundingById(fundingId);
        validateFundingAmount(funding, amount);
        validateFundingStatus(funding);

        final String orderDetailKey = orderNumberProvider.createOrderDetailKey();
        final FundingOrderDetail fundingOrderDetail = FundingOrderDetail.from(paymentFundingReadyRequest);
        redisUtils.save(orderDetailKey, fundingOrderDetail);

        final String name = funding.getProduct().getName();
        final PaymentReadyProductDto paymentReadyProductDto = new PaymentReadyProductDto(name, 1, amount);// TODO: 4/20/24 펀딩 결제는 단일 상품이므로 수량은 1개
        final KakaoPayReadyResponse kakaoPayReadyResponse = webClientService.ready(providerId, List.of(paymentReadyProductDto), orderDetailKey);
        return new PaymentReadyResponse(kakaoPayReadyResponse.tid(), kakaoPayReadyResponse.next_redirect_pc_url(), orderDetailKey);
    }

    @Transactional
    public PaymentGiftSuccessResponse approve(final String providerId,
                                              final PaymentSuccessRequest paymentSuccessRequest) {
        final KakaoPayApproveResponse approveResponse = webClientService.approve(providerId, paymentSuccessRequest);
        // approveResponse.partner_user_id() == providerId 검증 필요

        final Payment payment = saveAndGetPayment(approveResponse);
        final OrderDetails orderDetails = redisUtils.remove(approveResponse.partner_order_id(), OrderDetails.class);

        final String receiverProviderId = orderDetails.getReceiverProviderId();
        final Member recipient = findMemberByProviderId(providerId); // TODO: 3/30/24 토큰에 저장된 값이 PK가 아니라 Member 엔티티를 가져와야 함
        final Member receiver = findMemberByProviderId(receiverProviderId);

        final Receipts receipts = getReceipts(recipient.getMemberId(), receiver, orderDetails);
        saveGifts(receipts);
        saveOrders(payment, receipts);

        final List<OrderSummaryResponse> orderSummaries = getOrderSummaries(orderDetails);
        return new PaymentGiftSuccessResponse(PaymentSuccessReceiver.of(receiver, providerId), orderSummaries);
    }

    @Transactional
    public PaymentFundingSuccessResponse approveFunding(final String providerId,
                                                        final PaymentSuccessRequest paymentSuccessRequest) {
        final KakaoPayApproveResponse approveResponse = webClientService.approve(providerId, paymentSuccessRequest);
        final Payment payment = approveResponse.toEntity();
        final FundingOrderDetail fundingOrderDetail = redisUtils.remove(approveResponse.partner_order_id(), FundingOrderDetail.class);
        final Funding funding = findFundingById(fundingOrderDetail.fundingId());
        final Member member = findMemberByProviderId(providerId);
        final Long amount = payment.getTotalPrice();
        saveOrReflectFundingDetail(payment, funding, member, amount);
        funding.increaseAccumulateAmount(amount);

        // TODO: 5/10/24 결제 후 목표 금액 달성 시
        if (funding.satisfiedAccumulateAmount()) {
            funding.reflectStatus(amount, member.getMemberId());    // TODO: 5/14/24 잔여 금액 여부에 따라 "펀딩 완료", "남은 금액 결제 전"으로 상태를 수정
        }

        if (funding.completed()) {
            createAndSaveFundingGift(funding);
        }

        final Product product = funding.getProduct();
        final ProductSummaryResponse productSummaryResponse = ProductSummaryResponse.from(product);
        final PaymentSuccessReceiver paymentSuccessReceiver = PaymentSuccessReceiver.of(funding.getMember(), providerId);
        return new PaymentFundingSuccessResponse(paymentSuccessReceiver, productSummaryResponse, amount);
    }

    @Transactional
    public void cancel(final String providerId,
                       final PaymentCancelRequest paymentCancelRequest) {
        final Long paymentId = paymentCancelRequest.paymentId();
        final Order order = findOrderByPaymentId(paymentId);
        final Receipt receipt = order.getReceipt();
        validateMemberReceipt(providerId, receipt);

        final Long receiptId = receipt.getReceiptId();
        final Gift gift = findGiftByReceiptId(receiptId);
        validateAlreadyCanceled(order, Order::canceled);
        validateAlreadyCanceled(gift, Gift::canceled);

        order.cancel();
        gift.cancel();
        final PaymentCancelDto paymentCancelDto = findPaymentDtoById(paymentId);
        webClientService.cancel(paymentCancelDto);
    }

    @Transactional
    public void cancelFunding(final String providerId,
                              final PaymentFundingCancelRequest paymentFundingCancelRequest) {
        final Long fundingId = paymentFundingCancelRequest.fundingId();
        final Funding funding = findFundingById(fundingId);
        validateMemberFunding(providerId, funding);
        validateAlreadyCanceled(funding, Funding::canceled);
        final List<FundingDetail> fundingDetails = fundingDetailRepository.findAllByFundingId(fundingId);
        fundingDetails.forEach(fundingDetail -> refundFundingDetails(fundingDetail.getAmount(), fundingDetail));
        funding.cancel();
    }

    @Transactional
    public void cancelFundingDetail(final String providerId,
                                    final PaymentFundingDetailCancelRequest paymentFundingCancelRequest) {
        final Long fundingDetailId = paymentFundingCancelRequest.fundingDetailId();
        final FundingDetail fundingDetail = findFundingDetailById(fundingDetailId);
        validateAlreadyCanceled(fundingDetail, FundingDetail::canceled);
        validateMemberFundingDetail(providerId, fundingDetail);
        final Long amount = paymentFundingCancelRequest.amount();
        refundFundingDetails(amount, fundingDetail);
    }

    private Order findOrderByPaymentId(final Long paymentId) {
        return orderRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new OrderException(OrderErrorCode.NOT_FOUND));
    }

    private Gift findGiftByReceiptId(final Long receiptId) {
        return giftRepository.findByReceiptId(receiptId)
                .orElseThrow(() -> new GiftException(GiftErrorCode.NOT_FOUND));
    }

    private long getTotalProductAmount(final List<PaymentPreviewRequest> paymentPreviewRequests) {
        final List<Long> productIds = extractedProductIds(paymentPreviewRequests, PaymentPreviewRequest::productId);
        final Map<Long, Long> priceByIds = productRepository.findAllPriceByIdsGroupById(productIds);
        if (priceByIds == null || priceByIds.isEmpty()) {
            throw new ProductException(ProductErrorCode.NOT_FOUND);
        }

        return paymentPreviewRequests.stream()
                .mapToLong(paymentPreviewRequest -> priceByIds.get(paymentPreviewRequest.productId()) * paymentPreviewRequest.quantity())
                .sum();
    }

    private void validateReceiver(final String providerId,
                                  final PaymentGiftReadyReceiver paymentGiftReadyReceiver) {
        final String socialAccessToken = paymentGiftReadyReceiver.socialAccessToken();
        final String receiverProviderId = paymentGiftReadyReceiver.providerId();

        // TODO: 5/14/24 나에게 선물인 경우
        if (providerId.equals(receiverProviderId)) {
            return;
        }

        final boolean isFriend = kakaoFriendService.isFriend(socialAccessToken, receiverProviderId);
        if (!isFriend) {
            throw new FriendException(NOT_FOUND);
        }
    }

    private void validateTotalAmount(final List<PaymentGiftReadyItem> paymentGiftReadyItems) {
        final List<Long> productIds = extractedProductIds(paymentGiftReadyItems, PaymentGiftReadyItem::productId);
        final Map<Long, Long> priceByIds = productRepository.findAllPriceByIdsGroupById(productIds);
        if (priceByIds.isEmpty()) {
            throw new ProductException(ProductErrorCode.NOT_FOUND);
        }

        final boolean isAllMatch = paymentGiftReadyItems.stream()
                .anyMatch(paymentGiftReadyItem -> paymentGiftReadyItem.quantity() * priceByIds.get(paymentGiftReadyItem.productId()) == paymentGiftReadyItem.totalAmount());
        if (!isAllMatch) {
            throw new PaymentException(INVALID_AMOUNT);
        }
    }

    private List<PaymentReadyProductDto> getPaymentProductReadyRequests(final List<PaymentGiftReadyItem> paymentGiftReadyItems) {
        final List<Long> productIds = extractedProductIds(paymentGiftReadyItems, PaymentGiftReadyItem::productId);
        final Map<Long, String> nameById = productRepository.findAllNameByIdsGroupById(productIds);
        return paymentGiftReadyItems.stream()
                .map(paymentGiftReadyItem -> new PaymentReadyProductDto(nameById.get(paymentGiftReadyItem.productId()), paymentGiftReadyItem.quantity(), paymentGiftReadyItem.totalAmount()))
                .toList();
    }

    private void validateOptionDetailIds(final List<PaymentGiftReadyItem> paymentGiftReadyItems) {
        final boolean isAllMatch = paymentGiftReadyItems.stream()
                .anyMatch(this::matchesOptionsWithProduct);
        if (!isAllMatch) {
            throw new PaymentException(INVALID_OPTION);
        }
    }

    private boolean matchesOptionsWithProduct(final PaymentGiftReadyItem paymentGiftReadyItem) {
        final Long productId = paymentGiftReadyItem.productId();
        final List<Long> optionDetailIds = paymentGiftReadyItem.optionDetailIds();
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

    private OrderDetails getOrderDetails(final PaymentGiftReadyRequest paymentGiftReadyRequest) {
        final List<PaymentGiftReadyItem> paymentGiftReadyItems = paymentGiftReadyRequest.items();
        final String receiverProviderId = paymentGiftReadyRequest.receiver()
                .providerId();
        final List<OrderDetail> orderDetails = paymentGiftReadyItems.stream()
                .map(paymentGiftReadyItem -> paymentGiftReadyItem.toOrderDetail(orderNumberProvider.createOrderNumber()))
                .toList();
        return new OrderDetails(receiverProviderId, orderDetails);
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

    private void refundFundingDetails(final Long refundAmount,
                                      final FundingDetail fundingDetail) {
        final Payment payment = fundingDetail.getPayment();
        final Funding funding = fundingDetail.getFunding();
        funding.decreaseAccumulateAmount(refundAmount);

        final Long attributeAmount = fundingDetail.getAmount();
        // TODO: 4/27/24 전체 환불인 경우 상태 변경
        if (refundAmount.equals(attributeAmount)) {
            fundingDetail.cancel();
        }

        // TODO: 4/27/24 부분 환불인 경우(refundAmount < fundingDetail.getAmount())
        if (refundAmount < attributeAmount) {
            fundingDetail.partialCancel(refundAmount);
        }

        final PaymentCancelDto paymentCancelDto = PaymentCancelDto.of(payment, refundAmount);
        webClientService.cancel(paymentCancelDto);
    }

    private void createAndSaveFundingGift(final Funding funding) {
        final FundingGift fundingGift = FundingGift.builder()
                .funding(funding)
                .expiredAt(LocalDateTime.now().plusMonths(6L))
                .build();
        fundingGiftRepository.save(fundingGift);
    }

    private void validateFundingAmount(final Funding funding, final int attributeAmount) {
        if (!funding.isAttributableAmount(attributeAmount)) {
            throw new FundingException(INVALID_ATTRIBUTE_AMOUNT);
        }
    }

    private void validateFundingStatus(final Funding funding) {
        if (!funding.attributable()) {
            throw new FundingException(INVALID_STATUS);
        }
    }

    private <T> void validateAlreadyCanceled(final T item, final Function<T, Boolean> mapper) {
        if (mapper.apply(item)) {
            throw new PaymentException(ALREADY_REFUND);
        }
    }

    private void validateMemberFundingDetail(final String providerId, final FundingDetail fundingDetail) {
        if (!Objects.equals(fundingDetail.getMember().getProviderId(), providerId)) {
            throw new MemberException(NOT_FOUND);
        }
    }

    private void validateMemberFunding(final String providerId, final Funding funding) {
        if (!Objects.equals(funding.getMember().getProviderId(), providerId)) {
            throw new MemberException(NOT_FOUND);
        }
    }

    private void validateMemberReceipt(final String providerId, final Receipt receipt) {
        if (!Objects.equals(receipt.getRecipient().getProviderId(), providerId)) {
            throw new MemberException(NOT_FOUND);
        }
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

    private PaymentCancelDto findPaymentDtoById(final Long paymentId) {
        return paymentRepository.findCancelDtoById(paymentId)
                .orElseThrow(() -> new PaymentException(PaymentErrorCode.NOT_FOUND));
    }

    private FundingDetail findFundingDetailById(final Long fundingDetailId) {
        return fundingDetailRepository.findById(fundingDetailId)
                .orElseThrow(() -> new FundingDetailException(FundingDetailErrorCode.NOT_FOUND));
    }

    private void saveOrReflectFundingDetail(final Payment payment, final Funding funding, final Member member, final Long amount) {
        fundingDetailRepository.findByFundingAndMember(funding, member)
                .ifPresentOrElse(
                        fundingDetail -> fundingDetail.increaseAmountAndRate(amount),
                        () -> fundingDetailRepository.save(new FundingDetail(member, funding, payment))
                );
    }
}
