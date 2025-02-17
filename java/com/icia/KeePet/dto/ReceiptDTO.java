package com.icia.rmate.dto;

import com.icia.rmate.dto.PetDTO;
import lombok.Data;

@Data
public class ReceiptDTO {
    private BoardDTO boardDTO;   // Board 관련 데이터
    private PetDTO petDTO;       // Pet 관련 데이터
    private String paymentDate;  // 결제 일자
    private int paymentAmount;   // 결제 금액

}
