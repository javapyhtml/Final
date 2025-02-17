package com.icia.rmate.dto;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "REV")
@Data
public class RevEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REV_SEQ_GEN")
    @SequenceGenerator(name = "REV_SEQ_GEN", sequenceName = "REV_SEQ", allocationSize = 1)
    @Column(name = "REV_NUM")
    private int REVNum; // 후기 글 번호 (PK)

    @Column(name = "USER_ID")
    private String UserId;    // 후기 작성자 (FK)

    @ManyToOne // 추가: UserInfoEntity와의 관계 설정
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID", insertable = false, updatable = false)
    private UserInfoEntity UserInfo;

    @Column(name = "REV_CONTENTS", columnDefinition = "CLOB")
    private String RevContents; // 후기 내용

    @Column(name = "B_NUM")
    private int BNum;

    @ManyToOne
    @JoinColumn(name = "B_NUM", referencedColumnName = "B_NUM", insertable = false, updatable = false)
    private BoardEntity board;

    @Column(name = "REV_FILENAME")
    private String RevFileName; // 후기 첨부파일 이름

    @Column(name = "R_RATING")
    private int Rating; // 후기 평가

    // RevEntity 객체로 변환하는 메서드
    public static RevEntity toEntity(RevDTO dto) {
        RevEntity entity = new RevEntity();

        entity.setREVNum(dto.getREVNum());
        entity.setUserId(dto.getUserId());
        entity.setRevContents(dto.getRevContents());
        entity.setBNum(dto.getBNum());
        entity.setRevFileName(dto.getRevFileName());
        entity.setRating(dto.getRating());

        return entity;
    }
}