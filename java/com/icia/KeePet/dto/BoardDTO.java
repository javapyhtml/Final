package com.icia.rmate.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BoardDTO {

    private int BNum;         // 게시글 번호 (PK)
    private String UserId;    // 유저 아이디 (FK)
    private int PetNum;       // 반려견 등록번호(FK)
    private LocalDateTime BDate;  // 게시글 작성일
    private LocalDateTime BUpdate; // 게시글 수정일
    private String BTitle;    // 게시글 제목
    private String BContents;  // 상세 내용
    private MultipartFile BFile;    // 첨부파일
    private String BFileName; // 게시글 파일명
    private int RStatus; // ✅ null 허용

    private String GuardianName;    // 보호자 이름
    private String GuardianPhone;   // 보호자 연락처
    private String GuardianAddress; // 픽업 장소
    private String BServices;  // 다중 서비스 선택을 위한 String
    private int BTime;              // 예약시간(1:풀타임, 2:파트타임, 3:하프타임A, 4:하프타임B)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ReservationDate;  // 예약 날짜
    private int CategoryNum;
    private String PetName;
    private int PetStatus;

    // Board 엔티티 객체를 BoardDTO로 변환하는 메서드
    public static BoardDTO toDTO(BoardEntity entity) {
        BoardDTO dto = new BoardDTO();
        dto.setBNum(entity.getBNum());
        dto.setUserId(entity.getUserId());
        dto.setBDate(entity.getBDate());
        dto.setBUpdate(entity.getBUpdate());
        dto.setBTitle(entity.getBTitle());
        dto.setBContents(entity.getBContents());
        dto.setBFileName(entity.getBFileName());
        dto.setGuardianName(entity.getGuardianName());
        dto.setGuardianPhone(entity.getGuardianPhone());
        dto.setGuardianAddress(entity.getGuardianAddress());
        dto.setBServices(entity.getBServices());  // 서비스 값 가져오기
        dto.setBTime(entity.getBTime());
        dto.setReservationDate(entity.getReservationDate());
        dto.setPetNum(entity.getPetNum());
        dto.setRStatus(entity.getRStatus());
        dto.setCategoryNum(entity.getPet().getCategoryNum());
        dto.setPetName(entity.getPet().getPetName());
        dto.setPetStatus(entity.getPetStatus());

        return dto;
    }
}
