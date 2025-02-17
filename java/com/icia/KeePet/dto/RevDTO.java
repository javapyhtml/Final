package com.icia.rmate.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RevDTO {

    private int REVNum; // 후기 글번호
    private String UserId; // 유저 아이디
    private String RevContents; // 후기 내용
    private int BNum; // 게시글 번호
    private MultipartFile RevFile; //후기 파일
    private String RevFileName; // 후기 첨부파일 이름
    private int Rating; // 후기 평가
    private String BTitle;    // 게시글 제목

    // RevDTO 객체로 변환하는 메서드
    public static RevDTO toDTO(RevEntity entity) {
        RevDTO dto = new RevDTO();

        dto.setREVNum(entity.getREVNum());
        dto.setUserId(entity.getUserId()); // 유저 아이디 값
        dto.setRevContents(entity.getRevContents());
        dto.setBNum(entity.getBNum()); // 카테고리 번호 값
        dto.setRevFileName(entity.getRevFileName());
        dto.setRating(entity.getRating());
        dto.setBTitle(entity.getBoard().getBTitle());

        return dto;
    }
}