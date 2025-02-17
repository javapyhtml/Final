package com.icia.rmate.dto;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "APPLY")
public class ApplyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "APPLY_SEQ_GEN")
    @SequenceGenerator(name = "APPLY_SEQ_GEN", sequenceName = "APPLY_SEQ", allocationSize = 1)
    @Column(name = "A_NUM")
    private int ANum; // 신청 번호 (PK)
    @Column(name = "USER_ID")
    private String UserId; //
    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID", insertable = false, updatable = false )
    private UserInfoEntity UserInfo; // 유저 아이디 (FK)

    @Column(name = "B_NUM")
    private int BNum;

    @Column(name = "CATEGORY_NUM")
    private int CategoryNum;

    @ManyToOne
    @JoinColumn(name = "B_NUM", referencedColumnName = "B_NUM", insertable = false, updatable = false)
    private BoardEntity board; // 게시글 번호 (FK)

    @ManyToOne
    @JoinColumn(name = "CATEGORY_NUM", referencedColumnName = "CATEGORY_NUM", insertable = false, updatable = false)
    private CategoryEntity category; // 카테고리 번호 (FK)


    @Column(name = "P_STATUS", nullable = false)
    private int PStatus;    // 결제상태

    @Column(name = "R_STATUS")
    private int RStatus; // 리뷰 작성 여부 필드 추가

    @Column(name = "PET_NUM")
    private int PetNum;       // 반려견 등록번호(FK)

    @ManyToOne
    @JoinColumn(name = "PET_NUM", referencedColumnName = "PET_NUM", insertable = false, updatable = false)
    private PetEntity pet;


    // ApplyEntity 객체를 ApplyDTO로 변환하는 메서드
    public static ApplyEntity toEntity(ApplyDTO dto) {
        ApplyEntity entity = new ApplyEntity();
        entity.setUserId(dto.getUserId());
        entity.setBNum(dto.getBNum());
        entity.setPetNum(dto.getPetNum());
        entity.setCategoryNum(dto.getCategoryNum());
        entity.setPStatus(dto.getPStatus());
        entity.setRStatus(dto.getRStatus());
        return entity;
    }
}
