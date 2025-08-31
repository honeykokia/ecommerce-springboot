package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.bean.OrderItemBean;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemBean, Long> {

    List<OrderItemBean> findByOrderId(Long orderId);

}
