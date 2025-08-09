package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.bean.CategoryBean;
import com.example.demo.dto.CategoryInfo;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryBean, Long> {
    @Query("SELECT new com.example.demo.dto.CategoryInfo(c.id, c.name, c.description) FROM CategoryBean c")
    List<CategoryInfo> findAllCategoryInfo();
}

