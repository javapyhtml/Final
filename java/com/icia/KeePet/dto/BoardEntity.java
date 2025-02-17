package com.icia.rmate.dto;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "BOARD")
@Data
public class BoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UB_SEQ_GEN")
    @SequenceGenerator(name = "UB_SEQ_GEN", sequenceName = "UB_SEQ", allocationSize = 1)
    @Column(name = "B_NUM")
    private int BNum; // 게시글 번호 (PK)

    @Column(name = "PET_NUM")
    private int PetNum;       // 반려견 등록번호(FK)

    @ManyToOne
    @JoinColumn(name = "PET_NUM", referencedColumnName = "PET_NUM", insertable = false, updatable = false)
    private PetEntity pet; // 게시글 유형 (FK)

    @Column(name = "USER_ID")
    private String UserId;

    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID", insertable = false, updatable = false)
    private UserInfoEntity userInfo; // 유저 아이디 (FK)

    @CreationTimestamp
    @Column(name = "B_DATE")
    private LocalDateTime BDate; // 게시글 작성일

    @UpdateTimestamp
    @Column(name = "B_UPDATE")
    private LocalDateTime BUpdate;  // 게시글 수정일

    @Column(name = "B_TITLE")
    private String BTitle; // 게시글 제목

    @Column(name = "B_CONTENTS", columnDefinition = "CLOB")
    private String BContents; // 게시글 내용

    @Column(name = "B_FILENAME")
    private String BFileName; // 게시글 파일명

    @Column(name = "GUARDIAN_NAME")
    private String GuardianName;    // 보호자 이름

    @Column(name = "GUARDIAN_PHONE")
    private String GuardianPhone;   // 보호자 연락처

    @Column(name = "GUARDIAN_ADDRESS")
    private String GuardianAddress; // 보호자 주소

    @Column(name = "B_SERVICE")
    private String BServices;  // 다중 서비스 선택을 위한 String (쉼표로 구분된 서비스 목록)

    @Column(name = "B_TIME")
    private int BTime;              // 예약시간(0:풀타임, 1:파트타임, 2:하프타임A, 3:하프타임B)

    @Column(name = "RESERVATION_DATE")
    private Date ReservationDate;     // 예약날짜

    @Column(name = "R_STATUS")
    private int RStatus; // 리뷰 작성 여부 필드 추가

    @Column(name = "PET_STATUS")
    private int PetStatus;

    // BoardDTO 객체를 Board 엔티티로 변환하는 메서드
    public static BoardEntity toEntity(BoardDTO dto) {
        BoardEntity entity = new BoardEntity();

        entity.setBNum(dto.getBNum());
        entity.setUserId(dto.getUserId());
        entity.setBTitle(dto.getBTitle());
        entity.setBContents(dto.getBContents());
        entity.setBFileName(dto.getBFileName());
        entity.setBUpdate(dto.getBUpdate());
        entity.setBDate(dto.getBDate());
        entity.setGuardianName(dto.getGuardianName());
        entity.setGuardianPhone(dto.getGuardianPhone());
        entity.setGuardianAddress(dto.getGuardianAddress());
        entity.setBServices(dto.getBServices());  // 다중 서비스 값을 쉼표로 구분하여 설정
        entity.setBTime(dto.getBTime());
        entity.setReservationDate(dto.getReservationDate());
        entity.setPetNum(dto.getPetNum());
        entity.setRStatus(dto.getRStatus());
        entity.setPetStatus(dto.getPetStatus());

        return entity;
    }

}