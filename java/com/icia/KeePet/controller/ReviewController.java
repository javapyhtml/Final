package com.icia.rmate.controller;

import com.icia.rmate.dto.RevDTO;
import com.icia.rmate.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService rsvc;
    // RWriteForm :게시글 작성 폼 이동
    @GetMapping("/RWriteForm")
    public String bWriteForm(@RequestParam("bNum") int bNum, Model model) {
        model.addAttribute("bNum", bNum);
        System.out.println(bNum + "bnum확인:");
        return "review/reviewform"; // 뷰 이름 (review/reviewform.html 파일로 이동)
    }


    @PostMapping("/RWrite")
    @ResponseBody // JSON 응답을 반환하기 위해 추가
    public Map<String, Object> RWrite(@ModelAttribute RevDTO rev) {
        System.out.println("\n게시글작성\n[1] rev : " + rev);

        Map<String, Object> response = new HashMap<>();
        try {
            rsvc.RWrite(rev);
            response.put("success", true); // 성공 시 true
        } catch (Exception e) {
            System.out.println("리뷰 등록 실패!");
            response.put("success", false); // 실패 시 false
        }
        return response;
    }


    // RList : 리뷰 목록 페이지로 이동
    @GetMapping("/RList")
    public String RList() {
        return "/review/reviewboard";
    }

    // RView : 리뷰 상세보기
    @GetMapping("/revView/{REVNum}")
    public ModelAndView RView(@PathVariable int REVNum) {
        return rsvc.RView(REVNum);
    }

    // RModify : 리뷰 수정
    @PostMapping("/RModify")
    public ModelAndView RModify(@ModelAttribute RevDTO rev) {
        System.out.println("\n게시글 수정 메소드\n[1]html → controller : " + rev);
        return rsvc.RModify(rev);
    }

    // RDelete : 리뷰 삭제
    @GetMapping("/RDelete")
    public ModelAndView RDelete(@ModelAttribute RevDTO rev) {
        System.out.println("\n게시글 삭제 메소드\n[1]html → controller : " + rev);
        return rsvc.RDelete(rev);
    }
    // reportReview : 리뷰 신고
    @PostMapping("/reportReview")
    public ResponseEntity<String> reportReview(@RequestParam("REVNum") int REVNum, HttpServletRequest request, HttpServletResponse response) {
        // 로그인 아이디 가져오기
        String loginId = (String) request.getSession().getAttribute("loginId");

        // 로그인 아이디가 없으면 "Guest"로 설정
        if (loginId == null) {
            loginId = "Guest";
        }

        // 서비스에서 신고 처리 로직 호출
        String message = rsvc.reportReview(REVNum, loginId, request, response);

        // 클라이언트에게 처리 결과 메시지를 반환
        return ResponseEntity.ok(message);
    } // 컨트롤러



    @GetMapping("/myReviewList")
    @ResponseBody
    public ResponseEntity<List<RevDTO>> myReviewList(HttpServletRequest request) {

        // 로그인 아이디 가져오기
        String loginId = (String) request.getSession().getAttribute("loginId");

        if (loginId == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        List<RevDTO> myReviews = rsvc.getMyReviews(loginId);

        System.out.println("mypage 리뷰 dto 확인 ! " + myReviews);

        if (myReviews.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(myReviews, HttpStatus.OK);
    }

    @GetMapping("/getSessionUserId")
    @ResponseBody
    public ResponseEntity<Map<String, String>> getSessionUserId(HttpSession session) {
        // 세션에서 로그인된 사용자 ID 가져오기
        String loginId = (String) session.getAttribute("loginId");

        // 응답 데이터 생성
        Map<String, String> response = new HashMap<>();
        response.put("loginId", loginId);

        // ResponseEntity를 사용하여 JSON 응답 반환
        return ResponseEntity.ok(response);
    }
}
