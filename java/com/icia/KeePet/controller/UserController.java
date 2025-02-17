package com.icia.rmate.controller;

import com.icia.rmate.dto.UserInfoDTO;
import com.icia.rmate.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class UserController {

    private  final UserService usvc;

    private final HttpSession session;

    // index : 처음페이지로 이동
    @GetMapping("/")
    public String index(){
        return "index";
    }

//    // 유저 이용약관
//    @PostMapping("/mTermAgree")
//    public String termForm(@RequestParam List<Integer> TermsId, HttpSession session) {
//        // 약관 동의 내역을 세션에 저장
//        session.setAttribute("TermsId", TermsId);
//
//        // 약관 동의 완료 후 메인 페이지로 리다이렉트, URL 파라미터 추가
//        return "redirect:/?showModal=true";  // URL에 showModal=true 파라미터 추가
//    }
//
//    // mTermForm : 회원가입 약관페이지 이동
//    @GetMapping("/mTermForm")
//    public String mTermForm() {
//        return "termForm";
//    }

    // 유저 이용약관
    @PostMapping("/mTermAgree")
    public String termForm(@RequestParam List<Integer> TermsId, HttpSession session) {
        // 약관 동의 내역을 세션에 저장
        session.setAttribute("TermsId", TermsId);

        // 약관 동의 완료 후 메인 페이지로 리다이렉트, URL 파라미터 추가
        return "redirect:/?showModal=true";  // URL에 showModal=true 파라미터 추가
    }

    // mTermForm : 회원가입 약관페이지 이동
    @GetMapping("/mTermForm")
    public String mTermForm() {
        return "termForm";
    }

//    // 유저 이용약관 동의 처리
//    @PostMapping("/mTermAgree")
//    public String termForm(@RequestParam List<Integer> TermsId, HttpSession session, HttpServletRequest request) {
//        session.setAttribute("TermsId", TermsId);
//
//        String referer = request.getHeader("referer");
//        if (referer != null && referer.contains("cat")) {
//            return "redirect:/cat?showModal=join";  // 회원가입 모달을 띄우기 위해 join 추가
//        } else {
//            return "redirect:/?showModal=join";
//        }
//    }
//
//    // mTermForm : 회원가입 약관페이지 이동
//    @GetMapping("/mTermForm")
//    public String mTermForm(HttpServletRequest request) {
//        // 약관 페이지는 강아지, 고양이 페이지 모두 동일한 약관 페이지로 이동
//        return "termForm";  // 동일한 약관 페이지로 리디렉션
//    }

//    // 유저 이용약관 동의 처리
//    @PostMapping("/mTermAgree")
//    public String termForm(@RequestParam List<Integer> TermsId, HttpSession session, HttpServletRequest request) {
//        // 약관 동의 내역을 세션에 저장
//        session.setAttribute("TermsId", TermsId);
//
//        // referer 확인하여 현재 페이지에 맞게 리디렉션
//        String referer = request.getHeader("referer");
//        if (referer != null && referer.contains("cat")) {
//            return "redirect:/cat?showModal=true";  // 고양이 페이지로 리디렉션하면서 모달 표시
//        } else {
//            return "redirect:/?showModal=true";  // 강아지 페이지로 리디렉션하면서 모달 표시
//        }
//    }
//
//    // mTermForm : 회원가입 약관페이지 이동
//    @GetMapping("/mTermForm")
//    public String mTermForm(HttpServletRequest request) {
//        // 약관 페이지는 강아지, 고양이 페이지 모두 동일한 약관 페이지로 이동
//        return "termForm";  // 동일한 약관 페이지로 리디렉션
//    }


    //
//    // mJoinForm : 회원가입 페이지로 이동
//    @GetMapping("/mJoinForm")
//    public String mJoinForm(){
//        return "join";
//    }
//
//    // mLoginForm : 로그인 페이지로 이동
//    @GetMapping("/mLoginForm")
//    public String mLoginForm(){
//        return "login";
//    }
//
//    // mJoin : 회원가입
//    @PostMapping("/mJoin")
//    public ModelAndView mJoin(@ModelAttribute @Valid UserInfoDTO user, BindingResult bindingResult, HttpSession session) {
//        List<Integer> TermsId = (List<Integer>) session.getAttribute("TermsId");
//
//        // 약관 동의 여부 확인
//        if (TermsId == null || TermsId.isEmpty()) {
//            throw new RuntimeException("약관에 동의해야 합니다.");
//        }
//
//        // 유효성 검사 에러 확인
//        if (bindingResult.hasErrors()) {
//            ModelAndView mav = new ModelAndView("joinForm"); // 회원가입 폼으로 다시 이동
//            mav.addObject("user", user); // 기존 입력 데이터 유지
//            mav.addObject("errors", bindingResult.getAllErrors()); // 에러 정보 추가
//            return mav;
//        }
//
//        System.out.println("폼 작동 확인"+user+", "+ TermsId );
//        return usvc.mJoin(user, TermsId);
//    }
//
    // mdJoin : 회원수정
    @PostMapping("/mdJoin")
    public ModelAndView mdJoin(@ModelAttribute UserInfoDTO user){
        System.out.println("폼 작동 확인"+user );
        return usvc.mdJoin(user);
    }
// mJoin : 회원가입 처리

//    // 로그인 요청을 처리하는 컨트롤러 메서드
//    @PostMapping("/mLogin")
//    public ModelAndView mLogin(@ModelAttribute UserInfoDTO user, RedirectAttributes redirectAttributes) {
//        return usvc.mLogin(user, redirectAttributes);  // 서비스에서 수정된 mLogin 메서드를 호출
//    }

    // mJoin : 회원가입 처리
    @PostMapping("/mJoin")
    public ModelAndView mJoin(@ModelAttribute @Valid UserInfoDTO user, BindingResult bindingResult, HttpSession session, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();  // ModelAndView 객체 생성
        List<Integer> TermsId = (List<Integer>) session.getAttribute("TermsId");

        // 약관 동의 여부 확인
        if (TermsId == null || TermsId.isEmpty()) {
            throw new RuntimeException("약관에 동의해야 합니다.");
        }

        // 유효성 검사 에러 확인
        if (bindingResult.hasErrors()) {
            mav.setViewName("joinModal");  // 회원가입 폼으로 다시 모달 띄우기
            mav.addObject("user", user);  // 기존 입력 데이터 유지
            mav.addObject("errors", bindingResult.getAllErrors());  // 에러 정보 추가
            return mav;
        }

        // 회원가입 서비스 호출
        usvc.mJoin(user, TermsId);

        // referer 확인하여 현재 페이지에 맞게 리디렉션
        String referer = request.getHeader("referer");
        if (referer != null && referer.contains("cat")) {
            mav.setViewName("redirect:/cat?showModal=true");  // 고양이 페이지로 리디렉션하면서 모달 표시
        } else {
            mav.setViewName("redirect:/?showModal=true");  // 강아지 페이지로 리디렉션하면서 모달 표시
        }

        return mav;  // 수정된 mav 반환
    }


    // 로그인 요청을 처리하는 컨트롤러 메서드
    @PostMapping("/mLogin")
    public ModelAndView mLogin(@ModelAttribute UserInfoDTO user, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();  // ModelAndView 객체 생성

        // 로그인 서비스 호출
        mav = usvc.mLogin(user, redirectAttributes);  // 서비스에서 처리된 mLogin 메서드를 호출

        // 로그인 후 referer 확인하여 리디렉션
        String referer = request.getHeader("referer");
        if (referer != null && referer.contains("cat")) {
            // 고양이 페이지에서 로그인한 경우
            mav.setViewName("redirect:/cat");  // 고양이 페이지로 리디렉션
        } else {
            // 기본 강아지 페이지에서 로그인한 경우
            mav.setViewName("redirect:/");  // 강아지 페이지로 리디렉션
        }

        return mav;  // 수정된 mav 반환
    }


    // mLogout : 로그아웃
    @GetMapping("/mLogout")
    public String mLogout(HttpServletRequest request) {
        // 세션 무효화
        session.invalidate();

        // referer 확인하여 현재 페이지에 맞게 리디렉션
        String referer = request.getHeader("referer");
        if (referer != null && referer.contains("cat")) {
            return "redirect:/cat";  // 고양이 페이지로 리디렉션
        } else {
            return "redirect:/";  // 강아지 페이지로 리디렉션
        }
    }




    // 루트 URL로 접근하면 request-payment.html을 템플릿으로 렌더링하여 반환
    @GetMapping("/payForm")
    public String showPaymentPage() {
        // templates 폴더 내 pay.html 파일을 찾음
        return "pay";
    }


    //마이페이지로 이동
    @GetMapping("/myPageForm")
    public String myPage(){
        return "myPage";
    }

    //마이페이지로 이동
    @GetMapping("/adminPageForm")
    public String adminPage(){
        return "adminPage";
    }


    //회원 수정페이지로이동
    @GetMapping("/mdJoinForm")
    public String mdJoin(){
        return "mdJoin";
    }


    @GetMapping("/deleteUser")
    public String deleteUser(HttpServletRequest request) {
        String loginId = (String) request.getSession().getAttribute("loginId");

        if (loginId != null) {
            usvc.deleteUserAndRelatedData(loginId);
            request.getSession().invalidate();
            return "redirect:/"; // 홈페이지로 리다이렉트
        } else {
            return "redirect:/login"; // 로그인 페이지로 리다이렉트 (또는 다른 적절한 페이지)
        }
    }


}


