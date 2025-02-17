package com.icia.rmate.dao;

import com.icia.rmate.dto.UserInfoEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfoEntity, String> {
    Optional<UserInfoEntity> findByUserNickname(String userNickname);

    @Query("SELECT u FROM UserInfoEntity u WHERE u.UserId = :userId")
    UserInfoEntity findByUserId(String userId);

}



