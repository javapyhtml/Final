package com.icia.rmate.dto;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Entity
@Table(name = "PET")
public class PetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UB_SEQ_GEN")
    @SequenceGenerator(name = "UB_SEQ_GEN", sequenceName = "UB_SEQ", allocationSize = 1)
    @Column(name = "PET_NUM")
    private int PetNum;      // 반려견 등록번호(PK)

    @Column(name = "PET_NAME")
    private String PetName;     // 반려견 이름

    @Column(name = "PET_GENDER")
    private int PetGender;         // 반려견 성별

    @Column(name = "PET_NEUTERING")
    private int PetNeutering;         // 중성화 유무(0: 유, 1: 무)

    @Column(name = "PET_VACCIN")
    private int PetVaccin;         // 접종 유무(0: 유, 1: 무)

    @Column(name = "PET_AGE")
    private String PetAge;         // 반려견 나이

    @Column(name = "CATEGORY_NUM")
    private int CategoryNum;       // 반려견 크기(0: 퍼피, 1: 소형견, 2: 중형견, 3: 대형견)(FK)

    @ManyToOne
    @JoinColumn(name = "CATEGORY_NUM", referencedColumnName = "CATEGORY_NUM", insertable = false, updatable = false)
    private CategoryEntity category;

    @Column(name = "USER_ID")
    private String UserId;      // 보호자

    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID", insertable = false, updatable = false)
    private UserInfoEntity userInfo;

    @Column(name = "PET_PROFILENAME", nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'default.jpg'")
    private String PetProfileName;      // 반려견 프로필 이름

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    private List<BoardEntity> board;

//    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
//    private List<DiaryEntity> diary;

    public static PetEntity toEntity(PetDTO dto) {
        PetEntity entity = new PetEntity();

        entity.setPetNum(dto.getPetNum());
        entity.setPetName(dto.getPetName());
        entity.setPetGender(dto.getPetGender());
        entity.setPetNeutering(dto.getPetNeutering());
        entity.setPetVaccin(dto.getPetVaccin());
        entity.setPetAge(dto.getPetAge());
        entity.setCategoryNum(dto.getCategoryNum());
        entity.setUserId(dto.getUserId());
        entity.setPetProfileName(dto.getPetProfileName());

        return entity;
    }

}