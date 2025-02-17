package com.icia.rmate.dto;

import lombok.Data;

@Data
public class ApplyDTO {

    private int ANum; // 신청 번호 (PK)
    private String UserId; // 유저 아이디 (FK)
    private int BNum; // 게시글 번호 (FK)
    private int CategoryNum; // 카테고리 번호 (FK)
    private int PetNum;       // 반려견 등록번호(FK) 여기까지 직접가져와야하는 정보들 나머지는 .get을통해 가져온다.

    private int PStatus;    // 결제상태
    private int RStatus; // 리뷰 작성 여부 필드 추가

    private String GuardianName;    // 보호자 이름
    private String CategoryName; // 카테고리명
    private String BTitle;    // 게시글 제목

    private String PetName;     // 반려견 이름
    private int PetGender;         // 반려견 성별
    private int PetNeutering;         // 중성화 유무(0: 유, 1: 무)
    private int PetVaccin;         // 접종 유무(0: 유, 1: 무)
    private String PetAge;         // 반려견 나이(0: 1년 미만, 1: 성견)
    private String UserAddress; // 유저 주소 o
    private String PetProfileName;      // 반려견 프로필 이름4
    private int BTime;              // 예약시간(1:풀타임, 2:파트타임, 3:하프타임A, 4:하프타임B)
    private String BServices;  // 다중 서비스 선택을 위한 String (1:산책, 2:목욕, 3:병원, 4:픽업)


    // ApplyEntity 객체를 ApplyDTO로 변환하는 메서드
    public static ApplyDTO toDTO(ApplyEntity entity) {
        ApplyDTO dto = new ApplyDTO();
        dto.setGuardianName(entity.getBoard().getGuardianName());
        dto.setANum(entity.getANum());
        dto.setUserId(entity.getUserId());
        dto.setBNum(entity.getBNum()); // BoardEntity에서 B_Num 가져오기
        dto.setCategoryNum(entity.getCategoryNum()); // CategoryEntity에서 Category_Num 가져오기
        dto.setBTitle(entity.getBoard().getBTitle());
        dto.setPStatus(entity.getPStatus());
        dto.setRStatus(entity.getRStatus());
        dto.setCategoryName(entity.getCategory().getCategoryName());
        dto.setPetName(entity.getPet().getPetName());
        dto.setPetGender(entity.getPet().getPetGender());
        dto.setPetNeutering(entity.getPet().getPetNeutering());
        dto.setPetVaccin(entity.getPet().getPetVaccin());
        dto.setPetAge(entity.getPet().getPetAge());
        dto.setPetNum(entity.getPetNum());
        dto.setUserAddress(entity.getUserInfo().getUserAddress());
        dto.setPetProfileName(entity.getPet().getPetProfileName());
        dto.setBTime(entity.getBoard().getBTime());
        dto.setBServices(entity.getBoard().getBServices());
        return dto;
    }
}
