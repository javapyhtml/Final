package com.icia.rmate.dao;

import com.icia.rmate.dto.PetDTO;
import com.icia.rmate.dto.PetEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PetRepository extends JpaRepository<PetEntity, Long> {
    @Query("SELECT p FROM PetEntity p WHERE p.UserId = :userId")
    List<PetEntity> findByUserId(@Param("userId") String userId);

    @Query("SELECT p FROM PetEntity p WHERE p.PetNum = :petNum")
    Optional<PetEntity> findById(Integer petNum);


}

