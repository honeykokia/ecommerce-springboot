package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.bean.TagBean;

@Repository
public interface TagRepository extends JpaRepository<TagBean, Long> {

}
