package com.icia.rmate.controller;


import com.icia.rmate.dao.BoardRepository;
import com.icia.rmate.dao.PetRepository;
import com.icia.rmate.dto.*;
import com.icia.rmate.service.BoardService;
import com.icia.rmate.service.DiaryService;
import com.icia.rmate.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class DiaryController {

    @Autowired
    private DiaryService diaryService;

    @Autowired
    private BoardService boardService;

    @Autowired
    private UserService userService;
    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private PetRepository prepo;

    // 일지 작성 화면 (bNum으로 일지 작성 페이지 로드)
    @GetMapping("/writeForm")
    public String getWriteDiaryForm(@RequestParam("bNum") int bNum, HttpSession session, Model model) {
        String loginId = (String) session.getAttribute("loginId");
        int petNum = boardRepository.findPetNumByBNum(bNum);

        // boardEntity 조회
        BoardEntity boardEntity = boardRepository.findById(bNum).orElse(null);
        if (boardEntity == null) {
            return "redirect:/errorPage"; // 게시글 없으면 에러페이지로
        }

        PetEntity petEntity = prepo.findById(boardEntity.getPetNum()).orElse(null);
        if (petEntity == null) {
            return "redirect:/errorPage"; // 펫 정보 없으면 에러페이지로
        }

        String petName = petEntity.getPetName();
        Date reservationDate = boardEntity.getReservationDate();
        int bTime = boardEntity.getBTime();

        // 모델에 값 추가
        model.addAttribute("bNum", bNum);
        model.addAttribute("loginId", loginId);
        model.addAttribute("petNum", petNum);
        model.addAttribute("petName", petName);  // petName 추가
        model.addAttribute("reservationDate", reservationDate);  // reservationDate 추가
        model.addAttribute("bTime", bTime);  // bTime 추가

        return "diary/diaryForm"; // HTML 폼으로 반환
    }

    // 일지 작성
    @PostMapping("/diary/write")
    public String DiaryWrite(@ModelAttribute DiaryDTO diary, HttpSession session, RedirectAttributes redirectAttributes) {
        System.out.println("[1] diary : " + diary);  // diary 객체가 정상적으로 바인딩 되었는지 확인

        try {
            // BoardEntity 조회
            BoardEntity boardEntity = boardRepository.findById(diary.getBNum()).orElse(null);
            if (boardEntity == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "해당 게시글을 찾을 수 없습니다.");
                return "redirect:/errorPage";
            }

            // petNum을 이용해 PetEntity 조회
            PetEntity petEntity = prepo.findById(boardEntity.getPetNum()).orElse(null);
            String petName = (petEntity != null) ? petEntity.getPetName() : "알 수 없음";

            // BoardEntity에서 예약 날짜와 예약 시간 가져오기
            Date reservationDate = boardEntity.getReservationDate();
            int bTime = boardEntity.getBTime();

            // 다이어리 객체에 petName, reservationDate, bTime 세팅
            diary.setPetName(petName);
            diary.setReservationDate(reservationDate);
            diary.setBTime(bTime);

            // 사용자 ID 설정
            String userId = boardEntity.getUserId();
            diary.setUserId(userId);

            // 다이어리 작성 서비스 호출
            diaryService.DiaryWrite(diary);

            redirectAttributes.addFlashAttribute("successMessage", "리뷰 작성 완료");
            return "redirect:/"; // 일지 작성 후 리다이렉트할 페이지 경로
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "리뷰 작성 실패: " + e.getMessage());
            return "redirect:/errorPage";  // 에러 발생 시 리다이렉트할 페이지 경로
        }
    }

    // 사용자의 돌봄 일지 목록을 반환하는 API
    @GetMapping("/diaryList")
    @ResponseBody  // JSON 형식으로 반환하려면 @ResponseBody를 추가
    public List<DiaryDTO> getDiaryList(@RequestParam("userId") String userId) {
        System.out.println("[1] 컨트롤러 확인 : " + userId);
        // userId를 통해 해당 사용자의 일지 목록을 가져오기
        List<DiaryDTO> diaryList = diaryService.getDiariesByUserId(userId);
        return diaryList;
    }

//    @GetMapping("/list")
//    public String list() {
//        return "/diary/diaryBoard";
//    }


}

//    // 일지 수정
//    @PutMapping("/edit/{id}")
//    public String editDiary(@PathVariable int id, @Valid @RequestBody DiaryDTO diaryDTO) {
//        return diaryService.editDiary(id, diaryDTO);
//    }
//
//    // 일지 삭제
//    @DeleteMapping("/delete/{id}")
//    public String deleteDiary(@PathVariable int id) {
//        return diaryService.deleteDiary(id);
//    }
//
//    // 특정 일지 조회
//    @GetMapping("/{id}")
//    public DiaryDTO getDiary(@PathVariable int id) {
//        return diaryService.getDiary(id);
//    }



