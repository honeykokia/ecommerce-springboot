package com.example.demo.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.bean.CartItemBean;
import com.example.demo.bean.OrderBean;
import com.example.demo.bean.OrderItemBean;
import com.example.demo.dto.CheckoutRequest;
import com.example.demo.dto.ErrorInfo;
import com.example.demo.dto.OrderDetailInfo;
import com.example.demo.dto.OrderInfo;
import com.example.demo.enums.OrderStatus;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.responses.ApiResponse;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    public ApiResponse getOrders(){
        List<OrderBean> orders = orderRepository.findAll();
        List<OrderInfo> orderInfos = orders.stream()
            .map(this::convertToOrderInfo)
            .toList();
        return new ApiResponse(Map.of("orders", orderInfos));
    }

    @Transactional
    public ApiResponse createOrder(CheckoutRequest checkoutRequest) {

        List<CartItemBean> carts = cartItemRepository.findByCartId(checkoutRequest.getCartId());

        if (carts == null || carts.isEmpty()) {
            ErrorInfo errors = new ErrorInfo();
            errors.addError("cart", "Cart doesn't exist");
            throw new ApiException("Cart validation failed", 400, errors);
        }

        // Create a new order based on the checkout request and cart items
        OrderBean newOrder = new OrderBean();
        newOrder.setUserId(carts.get(0).getCart().getUser().getId());
        newOrder.setPaymentMethod(checkoutRequest.getPaymentMethod());
        newOrder.setShippingMethod(checkoutRequest.getShippingMethod());
        newOrder.setShippingAddress(checkoutRequest.getShippingAddress());
        newOrder.setStatus(OrderStatus.PENDING);
        newOrder.setCreatedAt(java.time.LocalDateTime.now());
        newOrder.setUpdatedAt(java.time.LocalDateTime.now());

        int totalPrice = addOrderItemsFromCart(newOrder, carts);
        newOrder.setTotalPrice(totalPrice);
        orderRepository.save(newOrder);

        OrderInfo orderInfo = convertToOrderInfo(newOrder);
        return new ApiResponse(Map.of("order", orderInfo));
    }

    public ApiResponse getOrderItems(Long orderId) {
        List<OrderItemBean> items = orderItemRepository.findByOrderId(orderId);
        List<OrderDetailInfo> itemDetails = items.stream()
            .map(this::convertToOrderDetailInfo)
            .toList();

        return new ApiResponse(Map.of("items", itemDetails));
    }

    public OrderInfo convertToOrderInfo(OrderBean order) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setId(order.getId());
        orderInfo.setUserId(order.getUserId());
        orderInfo.setTotalPrice(order.getTotalPrice());
        orderInfo.setStatus(order.getStatus());
        orderInfo.setCreatedAt(order.getCreatedAt());
        orderInfo.setUpdatedAt(order.getUpdatedAt());
        return orderInfo;
    }

    public OrderDetailInfo convertToOrderDetailInfo(OrderItemBean orderitem) {
        OrderDetailInfo orderDetailInfo = new OrderDetailInfo();
        orderDetailInfo.setOrderId(orderitem.getOrderId());
        orderDetailInfo.setProductId(orderitem.getProductId());
        orderDetailInfo.setProductName(orderitem.getProductName());
        orderDetailInfo.setQuantity(orderitem.getQuantity());
        orderDetailInfo.setUnitPrice(orderitem.getUnitPrice());
        orderDetailInfo.setTotalPrice(orderitem.getTotalPrice());
        return orderDetailInfo;
    }

    public int addOrderItemsFromCart(OrderBean order, List<CartItemBean> cartItems) {
        int total = 0;
        for (CartItemBean c : cartItems) {
            OrderItemBean oi = new OrderItemBean();
            oi.setProductId(c.getProduct().getId());                 
            oi.setProductName(c.getProduct().getName());      
            oi.setUnitPrice(c.getUnitPrice());                  
            oi.setQuantity(c.getQuantity());
            oi.setTotalPrice(c.getUnitPrice() * c.getQuantity());
            order.addItem(oi);
            total += oi.getTotalPrice();
        }
        return total;
    }   
}
