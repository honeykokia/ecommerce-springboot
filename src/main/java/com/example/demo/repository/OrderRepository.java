package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.bean.OrderBean;
import com.example.demo.enums.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<OrderBean, Long> {
    
    // Order management methods for admin
    @Query("SELECT o FROM OrderBean o ORDER BY o.createdAt DESC")
    List<OrderBean> findAllOrdersOrderByCreatedAt();
    
    // Additional admin-specific queries can be added here
    @Query("SELECT o FROM OrderBean o WHERE o.status = :status ORDER BY o.createdAt DESC")
    List<OrderBean> findOrdersByStatus(String status);

    OrderBean findByMerchantTradeNo(String merchantTradeNo);
    
    @Query("SELECT o.status FROM OrderBean o WHERE o.id = :id")
    Optional<OrderStatus> findStatusById(Long id);

    List<OrderBean> findByUserId(Long userId);
}
