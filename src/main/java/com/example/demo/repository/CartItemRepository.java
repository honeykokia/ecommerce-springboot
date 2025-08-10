package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.bean.CartItemBean;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemBean, Long> {
    List<CartItemBean> findByCartId(Long cartId);
    Optional<CartItemBean> findByCartIdAndProductId(Long cartId, Long productId);

}
