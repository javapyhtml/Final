package com.icia.rmate.dao;

import com.icia.rmate.dto.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {

    @Query("SELECT o FROM OrderEntity o WHERE o.UserId = :UserId")
    List<OrderEntity> findByUserId(@Param("UserId") String UserId);


    @Query("SELECT o FROM OrderEntity o WHERE o.Merchant_Uid = :merchantUid")
    Optional<OrderEntity> findByMerchantUid(@Param("merchantUid") String merchantUid);
}