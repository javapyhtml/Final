package com.icia.rmate.controller;

import com.icia.rmate.dto.*;
import com.icia.rmate.service.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class RestfulController {

    private final UserService usvc;
    private final PhoneService psvc;
    private final HttpSession session;
    private final BoardService bsvc;
    private final CommentService csvc;
    private final ReviewService rsvc;

    // idCheck : 아이디 중복 체크
    @PostMapping("/idCheck")
    public String idCheck(@RequestParam("UserId") String userId) {
        return usvc.idCheck(userId);
    }

    // emailCheck : 이메일 인증번호 받아오기
    @PostMapping("/emailCheck")
    public String emailCheck(@RequestParam("UserEmail") String userEmail) {
        return usvc.emailCheck(userEmail);
    }

    // PhoneCheck : 휴대폰 인증번호 받아오기
    @PostMapping("/PhoneCheck")
    public String phoneCheck(@RequestParam("UserPhone") String userPhone) {
        System.out.println("휴대전화 :" + userPhone);
        return psvc.phoneCheck(userPhone);
    }

    @PostMapping("/boardList")
    @ResponseBody
    public List<Object[]> boardList(@RequestParam(value = "petStatus", required = false) Integer petStatus, HttpSession session) {
        if (petStatus == null) {
            petStatus = 0;  // 기본값 설정
        }

        System.out.println("📢 요청된 petStatus: " + petStatus); // ✅ 서버에서 요청을 받는지 확인!

        List<Object[]> result = bsvc.boardList(new SearchOptionDTO(), petStatus);

        if (result == null || result.isEmpty()) {
            System.out.println("⚠️ 데이터가 없음! 빈 배열 반환");
            return new ArrayList<>();
        }

        System.out.println("✅ 반환할 데이터 개수: " + result.size());
        return result;
    }


    // revList : 게시판 목록 가져오기
    @PostMapping("/revList")
    public List<RevDTO> revList() {
        return rsvc.revList();
    }

    // bsearchList : 게시글 검색 목록 가져오기
    @PostMapping("/RsearchList")
    public List<RevDTO> rsearchList(@ModelAttribute SearchDTO search) {
        System.out.println("search : " + search); // 검색 조건 출력
        return rsvc.searchList(search);
    }

    // RestfulController
    @PostMapping("/nicknameCheck")
    public String nicknameCheck(@RequestParam("UserNickname") String userNickname) {
        System.out.println("nicknameCheck called with: " + userNickname); // 로깅
        return usvc.nicknameCheck(userNickname);
    }


}