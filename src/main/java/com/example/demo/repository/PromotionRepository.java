package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.bean.PromotionBean;
import com.example.demo.dto.PromotionInfo;


@Repository
public interface PromotionRepository extends JpaRepository<PromotionBean, Long> {

    @Query("SELECT new com.example.demo.dto.PromotionInfo(p.id, p.name, p.discountType, p.discountValue, p.description, p.imageUrl, p.isActive, p.startDate, p.endDate) FROM PromotionBean p")
    List<PromotionInfo> findAllPromotionInfo();
}
