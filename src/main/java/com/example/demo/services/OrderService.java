package com.example.demo.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.bean.CartBean;
import com.example.demo.bean.CartItemBean;
import com.example.demo.bean.OrderBean;
import com.example.demo.bean.OrderItemBean;
import com.example.demo.dto.CreateOrderRequest;
import com.example.demo.dto.CreateOrderResponse;
import com.example.demo.dto.ErrorInfo;
import com.example.demo.dto.OrderDetailInfo;
import com.example.demo.dto.OrderInfo;
import com.example.demo.dto.OrderSummary;
import com.example.demo.enums.CartStatus;
import com.example.demo.enums.OrderStatus;
import com.example.demo.enums.ShippingStatus;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.responses.ApiResponse;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    public ApiResponse getOrders(Long userId){
        List<OrderBean> orders = orderRepository.findByUserId(userId);
        List<OrderInfo> orderInfos = orders.stream()
            .map(this::convertToOrderInfo)
            .toList();
        return new ApiResponse(Map.of("orders", orderInfos));
    }

    public ApiResponse getOrderStatusById(Long orderId){
        Optional<OrderStatus> orderStatusOpt = orderRepository.findStatusById(orderId);

        if (orderStatusOpt.isEmpty()) {
            ErrorInfo errors = new ErrorInfo();
            errors.addError("order", "Order not found");
            throw new ApiException("Order not found", 404, errors);
        }
        return new ApiResponse(Map.of("order", Map.of(
            "status", orderStatusOpt.get().name()
        )));
    }

    @Transactional
    public ApiResponse createOrder(Long userId,CreateOrderRequest request) {
        Optional<CartBean> cartOpt = cartRepository.findByUserIdAndStatus(userId, CartStatus.ACTIVE);
        CartBean  cart = cartOpt.orElseThrow(() -> {
            ErrorInfo errors = new ErrorInfo();
            errors.addError("cart", "Cart doesn't exist");
            throw new ApiException("Cart validation failed", 400, errors);
        });

        List<CartItemBean> carts = cartItemRepository.findByCartId(cart.getId());

        if (carts == null || carts.isEmpty()) {
            ErrorInfo errors = new ErrorInfo();
            errors.addError("cart", "Cart not added");
            throw new ApiException("Cart validation failed", 400, errors);
        }


        // Create a new order based on the checkout request and cart items
        OrderBean newOrder = new OrderBean();
        newOrder.setUser(carts.get(0).getCart().getUser());

        newOrder.setPaymentMethod(request.getPaymentMethod());
        newOrder.setShippingMethod(request.getShippingMethod());
        newOrder.setShippingAddress(request.getShippingAddress());
        newOrder.setShippingStatus(ShippingStatus.PENDING);
        newOrder.setStatus(OrderStatus.PENDING);
        newOrder.setCreatedAt(java.time.LocalDateTime.now());
        newOrder.setUpdatedAt(java.time.LocalDateTime.now());
        newOrder.setCurrency("TWD");

        String merchantTradeNo = ensureUniqueTradeNo("ORD");
        newOrder.setMerchantTradeNo(merchantTradeNo);
        
        OrderSummary summary = addOrderItemsFromCart(newOrder, carts);
        int AmountCents = summary.getAmount();
        newOrder.setAmountCents(AmountCents);
        newOrder.setTradeDesc(request.getTradeDesc());
        orderRepository.save(newOrder);

        CreateOrderResponse response = new CreateOrderResponse();
        response.setOrderId(newOrder.getId());
        response.setMerchantTradeNo(newOrder.getMerchantTradeNo());
        response.setAmountCents(newOrder.getAmountCents());
        response.setItemName(summary.getItemName());
        response.setTradeDesc(newOrder.getTradeDesc());
        return new ApiResponse(Map.of("order", response));
    }

    public ApiResponse getOrderItems(Long orderId) {
        List<OrderItemBean> items = orderItemRepository.findByOrderId(orderId);
        List<OrderDetailInfo> itemDetails = items.stream()
            .map(this::convertToOrderDetailInfo)
            .toList();

        return new ApiResponse(Map.of("items", itemDetails));
    }

    public String ensureUniqueTradeNo(String orderId) {
        return (orderId + System.currentTimeMillis()).substring(0, Math.min(20, (orderId + System.currentTimeMillis()).length()));
    }

    public OrderInfo convertToOrderInfo(OrderBean order) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setId(order.getId());
        orderInfo.setUserId(order.getUser().getId());
        orderInfo.setMerchantTradeNo(order.getMerchantTradeNo());
        orderInfo.setAmountCents(order.getAmountCents());
        orderInfo.setCurrency(order.getCurrency());
        orderInfo.setStatus(order.getStatus());
        orderInfo.setPaymentMethod(order.getPaymentMethod());
        orderInfo.setPaidAt(order.getPaidAt());
        orderInfo.setCancelledAt(order.getCancelledAt());
        orderInfo.setShippingMethod(order.getShippingMethod());
        orderInfo.setShippingAddress(order.getShippingAddress());
        orderInfo.setShippingStatus(order.getShippingStatus());
        orderInfo.setCreatedAt(order.getCreatedAt());
        orderInfo.setUpdatedAt(order.getUpdatedAt());
        return orderInfo;
    }

    public OrderDetailInfo convertToOrderDetailInfo(OrderItemBean orderitem) {
        OrderDetailInfo orderDetailInfo = new OrderDetailInfo();
        orderDetailInfo.setOrderId(orderitem.getOrder().getId());
        orderDetailInfo.setProductId(orderitem.getProductId());
        orderDetailInfo.setProductName(orderitem.getProductName());
        orderDetailInfo.setQuantity(orderitem.getQuantity());
        orderDetailInfo.setUnitPrice(orderitem.getUnitPrice());
        orderDetailInfo.setTotalPrice(orderitem.getTotalPrice());
        return orderDetailInfo;
    }

    public OrderSummary addOrderItemsFromCart(OrderBean order, List<CartItemBean> cartItems) {
        Integer amount = 0;
        String itemName = "";
        for (CartItemBean c : cartItems) {
            OrderItemBean oi = new OrderItemBean();
            oi.setProductId(c.getProduct().getId());                 
            oi.setProductName(c.getProduct().getName());      
            oi.setUnitPrice(c.getUnitPrice());                  
            oi.setQuantity(c.getQuantity());
            oi.setTotalPrice(c.getUnitPrice() * c.getQuantity());
            order.addItem(oi);
            amount += oi.getTotalPrice();
            itemName += oi.getProductName() + "x" + oi.getQuantity() + "#";
        }

        return new OrderSummary(amount, itemName);
    }
}
