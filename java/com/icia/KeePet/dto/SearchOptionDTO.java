package com.icia.rmate.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SearchOptionDTO {

//    private String UserId;  // 지역 ID -> 이제 DETAIL_NUM으로 변경
    private Integer categoryType;  // 카테고리 타입
    private String keyword;     // 키워드

    // 생성자, 필요시 다른 메서드를 추가할 수 있음
}
