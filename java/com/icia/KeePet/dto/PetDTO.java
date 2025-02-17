package com.icia.rmate.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PetDTO {
    private int PetNum;      // 반려견 등록번호(PK)
    private String PetName;     // 반려견 이름
    private int PetGender;         // 반려견 성별
    private int PetNeutering;      // 중성화 유무(0: 유, 1: 무)
    private int PetVaccin;         // 접종 유무(0: 유, 1: 무)
    private String PetAge;         // 반려견 나이
    private int CategoryNum;  // 반려견 크기(0: 퍼피, 1: 소형견, 2: 중형견, 3: 대형견)(FK)
    private String UserId;    // 보호자
    private MultipartFile PetProfile;   // 반려견 프로필
    private String PetProfileName;      // 반려견 프로필 이름


    public static PetDTO toDTO(PetEntity entity) {
        PetDTO dto= new PetDTO();

        dto.setPetNum(entity.getPetNum());
        dto.setPetName(entity.getPetName());
        dto.setPetGender(entity.getPetGender());
        dto.setPetNeutering(entity.getPetNeutering());
        dto.setPetVaccin(entity.getPetVaccin());
        dto.setPetAge(entity.getPetAge());
        dto.setCategoryNum(entity.getCategoryNum());
        dto.setUserId(entity.getUserId());
        dto.setPetProfileName(entity.getPetProfileName());

        return dto;
    }
}
