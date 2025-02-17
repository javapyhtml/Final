package com.icia.rmate.dao;

import com.icia.rmate.dao.base.Page;
import com.icia.rmate.dto.BoardDTO;
import com.icia.rmate.dto.BoardEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Integer> {

    @Query("SELECT b FROM BoardEntity b WHERE b.UserId = :UserId")
    List<BoardEntity> findByUserIdQuery(@Param("UserId") String UserId);

    @Query("SELECT b FROM BoardEntity b WHERE b.UserId = :UserId")
    List<BoardEntity> findByUserId(@Param("UserId")String loginId);

//    @Modifying
//    @Transactional
//    @Query("UPDATE BoardEntity b SET b.CategoryNum = 4 WHERE b.BNum = :bNum")
//    void boardToHistory(@Param("bNum") int bNum);

    List<BoardEntity> findAllByOrderByBNumDesc();

    List<BoardEntity> findAll();

    // 게시글 번호(BNum)으로 게시글 정보 찾기
    BoardEntity findByBNum(int bNum);

    @Query(value = "SELECT B.B_NUM, B.B_TITLE, B.B_FILENAME, U.USER_ID " +
            "FROM BOARD B " +
            "JOIN USER_INFO U ON B.USER_ID = U.USER_ID " +
            "AND (:keyword IS NULL OR B.B_TITLE LIKE '%' || :keyword || '%' OR B.B_CONTENTS LIKE '%' || :keyword || '%') " +
            "ORDER BY B.B_DATE DESC", nativeQuery = true)
    List<Object[]> searchBoardsWithJoin(@Param("keyword") String keyword);

    @Query(value = "SELECT B.B_NUM, B.B_TITLE, B.B_FILENAME, U.USER_ID " +
            "FROM BOARD B " +
            "JOIN USER_INFO U ON B.USER_ID = U.USER_ID " +
            "WHERE B.PET_STATUS = :petStatus " +  // petStatus 필터링 확실히 적용
            "AND (:keyword IS NULL OR B.B_TITLE LIKE '%' || :keyword || '%' OR B.B_CONTENTS LIKE '%' || :keyword || '%') " +
            "ORDER BY B.B_DATE DESC", nativeQuery = true)
    List<Object[]> searchBoardsWithPetStatus(@Param("keyword") String keyword, @Param("petStatus") int petStatus);

    @Query("SELECT b.PetNum FROM BoardEntity b WHERE b.BNum = :bNum")
    Integer findPetNumByBNum(@Param("bNum") int bNum);

    @Modifying
    @Transactional
    @Query("UPDATE BoardEntity a SET a.RStatus = 1 WHERE a.BNum = :bNum and a.UserId = :UserId")
    void saveRStatus(@Param("bNum") int bNum,@Param("UserId") String UserId);


    @Query("SELECT b FROM BoardEntity b WHERE b.BNum = :bNum and b.UserId = :UserId and b.RStatus = :rStatus")
    BoardEntity searchRStatus(@Param("bNum") int bNum, @Param("UserId") String UserId, @Param("rStatus") int rStatus);



    // Custom query to find boards by BServices
    List<BoardEntity> findByBServices(String bServices);


}