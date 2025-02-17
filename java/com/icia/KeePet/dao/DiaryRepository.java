package com.icia.rmate.dao;

import com.icia.rmate.dto.DiaryDTO;
import com.icia.rmate.dto.DiaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiaryRepository extends JpaRepository<DiaryEntity, Integer> {
    List<DiaryEntity> findByUserId(String userId);
}
