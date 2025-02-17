package com.icia.rmate.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
public class DiaryDTO {
    @NotNull(message = "방문 시간은 필수입니다.")
    private int DNum; // 일지 번호 (PK)

    @NotNull(message = "방문 시간은 필수입니다.")
    private Integer BNum; // 게시글 번호 (FK)

    @NotNull(message = "방문 시간은 필수입니다.")
    private String UserId; // 사용자 ID (FK)

    private int PetNum; // 반려견 등록번호(FK)

    @NotNull(message = "방문 시간은 필수입니다.")
    private String meal; // 식사 정보

    @NotNull(message = "방문 시간은 필수입니다.")
    private String snack; // 간식 정보

    @NotNull(message = "방문 시간은 필수입니다.")
    private String walk; // 산책 정보

    @NotNull(message = "방문 시간은 필수입니다.")
    private String play; // 놀이 정보

    @NotNull(message = "방문 시간은 필수입니다.")
    private String toilet; // 배변 정보

    @NotNull(message = "방문 시간은 필수입니다.")
    private String sleep; // 잠 정보

    private String PetName;
    private int BTime;              // 예약시간(1:풀타임, 2:파트타임, 3:하프타임A, 4:하프타임B)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ReservationDate;  // 예약 날짜 (NEW!)

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String visitDate; // 뷰에서 사용할 방문 날짜 (NEW!)
    private String visitTime; // 뷰에서 사용할 방문 시간 (NEW!)


    private String hospital; // 병원 정보
    private String bath; // 목욕 정보
    private String PetPlayProfileName; // 유저 프로필 이름 o
    private MultipartFile PetPlayProfile;

    /**
     * DiaryEntity 객체를 DiaryDTO로 변환하는 메서드
     */
    public static DiaryDTO toDTO(DiaryEntity entity) {
        DiaryDTO dto = new DiaryDTO();
        dto.setDNum(entity.getDNum());
        dto.setBNum(entity.getBNum());
        dto.setUserId(entity.getUserId());
        dto.setMeal(entity.getMeal());
        dto.setSnack(entity.getSnack());
        dto.setWalk(entity.getWalk());
        dto.setPlay(entity.getPlay());
        dto.setToilet(entity.getToilet());
        dto.setSleep(entity.getSleep());
        dto.setHospital(entity.getHospital());
        dto.setBath(entity.getBath());
        dto.setPetPlayProfileName(entity.getPetPlayProfileName());
        dto.setPetNum(entity.getPetNum());
        dto.setPetName(entity.getPet().getPetName()); // PetEntity에서 petName 설정 (추가 및 수정!)
        dto.setReservationDate(entity.getBoard().getReservationDate()); // Date 타입 예약 날짜 설정 (NEW!)
        dto.setVisitDate(entity.getBoard().getReservationDate() != null ? entity.getBoard().getReservationDate().toString() : null); // 뷰에서 사용할 방문 날짜 (String 변환) (NEW!)
        dto.setVisitTime(String.valueOf(entity.getBoard().getBTime())); // 뷰에서 사용할 방문 시간 (String 변환) (NEW!)
        dto.setBTime(entity.getBoard().getBTime());


        return dto;
    }

}