package com.icia.rmate.dto;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "DIARY")
@Data
public class DiaryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DIARY_SEQ_GEN")
    @SequenceGenerator(name = "DIARY_SEQ_GEN", sequenceName = "DIARY_SEQ", allocationSize = 1)
    @Column(name = "D_NUM")
    private int DNum; // 일지 번호 (PK)

    @Column(name = "B_NUM")
    private Integer BNum; // 게시글 번호 (FK)

    @Column(name = "USER_ID")
    private String userId;  // 실제 FK 컬럼

    // userId를 외래 키로 사용하는 UserInfoEntity
    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID", insertable = false, updatable = false)
    private UserInfoEntity userInfo;  // UserInfoEntity와의 관계

    @ManyToOne
    @JoinColumn(name = "B_NUM", referencedColumnName = "B_NUM", insertable = false, updatable = false)
    private BoardEntity board; // BoardEntity와의 관계 설정

    @ManyToOne
    @JoinColumn(name = "PET_NUM", referencedColumnName = "PET_NUM", insertable = false, updatable = false)
    private PetEntity pet; // 게시글 유형 (FK)

    @Column(name = "PET_NUM")
    private int PetNum;

    @Column(name = "MEAL")
    private String meal; // 식사 정보

    @Column(name = "SNACK")
    private String snack; // 간식 정보

    @Column(name = "WALK")
    private String walk; // 산책 정보

    @Column(name = "PLAY")
    private String play; // 놀이 정보

    @Column(name = "TOILET")
    private String toilet; // 배변 정보

    @Column(name = "SLEEP")
    private String sleep; // 잠 정보

    @Column(name = "HOSPITAL")
    private String hospital; // 병원 정보

    @Column(name = "BATH")
    private String bath; // 목욕 정보

    @CreationTimestamp
    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate; // 작성 날짜

    @UpdateTimestamp
    @Column(name = "UPDATED_DATE")
    private LocalDateTime updatedDate; // 수정 날짜

    @Column(name = "PET_PLAYNAME", nullable = false, length = 255, columnDefinition = "VARCHAR(255) DEFAULT 'default.jpg'")
    private String PetPlayProfileName; // 유저 프로필 이름

    /**
     * DiaryDTO 객체를 DiaryEntity로 변환하는 메서드
     */
    public static DiaryEntity toEntity(DiaryDTO dto) {
        DiaryEntity entity = new DiaryEntity();
        entity.setDNum(dto.getDNum());
        entity.setBNum(dto.getBNum());
        entity.setUserId(dto.getUserId());
        entity.setMeal(dto.getMeal());
        entity.setSnack(dto.getSnack());
        entity.setWalk(dto.getWalk());
        entity.setPlay(dto.getPlay());
        entity.setToilet(dto.getToilet());
        entity.setSleep(dto.getSleep());
        entity.setHospital(dto.getHospital());
        entity.setBath(dto.getBath());
        entity.setPetPlayProfileName(dto.getPetPlayProfileName());
        entity.setPetNum(dto.getPetNum());
        return entity;
    }
}
