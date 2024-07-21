package org.kakaoshare.backend.domain.order.service;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.common.dto.PageResponse;
import org.kakaoshare.backend.domain.option.dto.OptionSummaryRequest;
import org.kakaoshare.backend.domain.option.repository.OptionDetailRepository;
import org.kakaoshare.backend.domain.order.dto.inquiry.OrderHistoryDetailDto;
import org.kakaoshare.backend.domain.order.dto.inquiry.request.OrderHistoryRequest;
import org.kakaoshare.backend.domain.order.dto.inquiry.response.OrderHistoryDetailResponse;
import org.kakaoshare.backend.domain.order.dto.inquiry.response.OrderHistoryResponse;
import org.kakaoshare.backend.domain.order.dto.preview.OrderPreviewRequest;
import org.kakaoshare.backend.domain.order.dto.preview.OrderPreviewResponse;
import org.kakaoshare.backend.domain.order.exception.OrderErrorCode;
import org.kakaoshare.backend.domain.order.exception.OrderException;
import org.kakaoshare.backend.domain.order.repository.OrderRepository;
import org.kakaoshare.backend.domain.order.vo.OrderHistoryDate;
import org.kakaoshare.backend.domain.payment.dto.inquiry.PaymentHistoryDto;
import org.kakaoshare.backend.domain.payment.exception.PaymentErrorCode;
import org.kakaoshare.backend.domain.payment.exception.PaymentException;
import org.kakaoshare.backend.domain.payment.repository.PaymentRepository;
import org.kakaoshare.backend.domain.product.dto.ProductDto;
import org.kakaoshare.backend.domain.product.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class OrderService {
    private final ProductRepository productRepository;
    private final OptionDetailRepository optionDetailRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    public PageResponse<?> preview(final List<OrderPreviewRequest> orderPreviewRequests,
                                   final Pageable pageable) {
        final Map<Long, List<String>> optionNamesGroupByProductId = getOptionsGroupByProductId(orderPreviewRequests);
        final Map<Long, Integer> quantityGroupByProductId = getQuantityGroupByProductId(orderPreviewRequests);
        final Page<ProductDto> productDtos = getProductDtos(orderPreviewRequests, pageable);
        final Page<OrderPreviewResponse> page = productDtos
                .map(productDto -> new OrderPreviewResponse(
                        productDto,
                        optionNamesGroupByProductId.get(productDto.getProductId()),
                        quantityGroupByProductId.get(productDto.getProductId()))
                );
        return PageResponse.from(page); // TODO: 4/15/24 동일한 상품이 여러 개인 경우를 처리하지 못함
    }

    public PageResponse<?> lookUp(final String providerId,
                                  final OrderHistoryRequest orderHistoryRequest,
                                  final Pageable pageable) {
        final OrderHistoryDate date = orderHistoryRequest.toDate();
        final Page<OrderHistoryResponse> page = orderRepository.findAllOrderProductDtoByCondition(providerId, date, pageable)
                .map(orderProductDto -> OrderHistoryResponse.of(orderProductDto, providerId));
        return PageResponse.from(page);
    }

    public OrderHistoryDetailResponse lookUpDetail(final Long orderId) {
        final OrderHistoryDetailDto orderHistoryDetailDto = findOrderHistoryDetailByOrderId(orderId);
        final PaymentHistoryDto paymentHistoryDto = findPaymentHistoryDtoByOrderId(orderId);
        return new OrderHistoryDetailResponse(orderHistoryDetailDto, paymentHistoryDto);
    }

    private Page<ProductDto> getProductDtos(final List<OrderPreviewRequest> orderPreviewRequests, final Pageable pageable) {
        final List<Long> productIds = extractedProductIds(orderPreviewRequests);
        return productRepository.findAllByProductIds(productIds, pageable);
    }

    private Map<Long, Integer> getQuantityGroupByProductId(final List<OrderPreviewRequest> orderPreviewRequests) {
        return orderPreviewRequests.stream()
                .collect(Collectors.toMap(
                        OrderPreviewRequest::productId,
                        OrderPreviewRequest::quantity
                ));
    }

    private Map<Long, List<String>> getOptionsGroupByProductId(final List<OrderPreviewRequest> orderPreviewRequests) {
        return orderPreviewRequests.stream()
                .collect(Collectors.toMap(
                        OrderPreviewRequest::productId,
                        orderPreviewRequest -> optionDetailRepository.findNamesByIds(extractedOptionDetailIds(orderPreviewRequest))
                ));
    }

    private List<Long> extractedOptionDetailIds(final OrderPreviewRequest orderPreviewRequest) {
        return orderPreviewRequest.options()
                .stream()
                .map(OptionSummaryRequest::detailId)
                .toList();
    }

    private List<Long> extractedProductIds(final List<OrderPreviewRequest> orderPreviewRequests) {
        return orderPreviewRequests.stream()
                .map(OrderPreviewRequest::productId)
                .toList();
    }

    private OrderHistoryDetailDto findOrderHistoryDetailByOrderId(final Long orderId) {
        return orderRepository.findHistoryDetailById(orderId)
                .orElseThrow(() -> new OrderException(OrderErrorCode.NOT_FOUND));
    }

    private PaymentHistoryDto findPaymentHistoryDtoByOrderId(final Long orderId) {
        return paymentRepository.findHistoryByOrderId(orderId)
                .orElseThrow(() -> new PaymentException(PaymentErrorCode.NOT_FOUND));
    }
}
