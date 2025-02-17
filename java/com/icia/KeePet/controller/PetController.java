package com.icia.rmate.controller;

import ch.qos.logback.core.model.Model;
import com.icia.rmate.dto.PetDTO;
import com.icia.rmate.dto.PetEntity;
import com.icia.rmate.dto.UserInfoDTO;
import com.icia.rmate.dto.UserInfoEntity;
import com.icia.rmate.service.PetService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class PetController {

    private final PetService psvc;

    Path path = Paths.get(System.getProperty("user.dir"), "src/main/resources/static/pet");

    // 반려견 정보 등록
    @PostMapping("/registerPet")
    @ResponseBody
    public Map<String, Object> registerPet(@ModelAttribute PetDTO pet, @RequestParam("PetProfile") MultipartFile petProfile) {
        Map<String, Object> response = new HashMap<>();

        // (1) 파일 업로드 처리
        if (petProfile != null && !petProfile.isEmpty()) {
            String uuid = UUID.randomUUID().toString().substring(0, 8);  // 고유한 UUID 생성
            String fileName = petProfile.getOriginalFilename();  // 원래 파일 이름
            String petProfileName = uuid + "_" + fileName;  // 파일 이름에 UUID 추가

            pet.setPetProfileName(petProfileName);  // DTO에 파일 이름 저장

            // 저장 경로 설정
            String savePath = path.toString() + "\\" + petProfileName;  // 경로 + 파일 이름
            System.out.println("savePath : " + savePath);  // 경로 확인

            try {
                // 파일을 해당 경로에 저장
                petProfile.transferTo(new File(savePath));
            } catch (IOException e) {
                throw new RuntimeException("파일 저장 중 오류 발생", e);  // 예외 처리
            }
        } else {
            // 파일이 업로드되지 않은 경우 기본 이미지 설정
            pet.setPetProfileName("default.jpg");
        }

        try {
            // 반려견 등록 처리
            boolean isRegistered = psvc.registerPet(pet);  // 서비스 호출
            if (isRegistered) {
                response.put("success", true);
                response.put("message", "반려견이 등록되었습니다.");
            } else {
                response.put("success", false);
                response.put("message", "반려견 등록에 실패했습니다.");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "반려견 등록 중 오류가 발생했습니다: " + e.getMessage());
            e.printStackTrace();  // 예외 로그 출력
        }

        return response;  // JSON 형태로 응답 반환
    }

    // 마이페이지에서 반려견 정보를 불러오기
    @GetMapping("/getPetInfo")
    @ResponseBody
    public List<PetDTO> getPetInfo(HttpSession session) {
        String userId = (String) session.getAttribute("loginId");

        // 로그인되지 않은 경우 빈 리스트 반환
        if (userId == null) {
            return Collections.emptyList();
        }

        // 서비스 호출하여 반려견 정보 리스트 가져오기
        List<PetDTO> pets = psvc.getPetInfo(userId); // 사용자 ID로 반려견 정보 조회
        System.out.println(pets);

        // 반려견 사진 경로 추가
        for (PetDTO pet : pets) {
            // 기본 경로 설정 (이미지가 서버에 저장된 위치)
            String petProfileImagePath = "/pet/" + pet.getPetProfileName();  // static/pet 경로에 저장됨
            pet.setPetProfileName(petProfileImagePath);  // 이미지 URL 설정
        }

        return pets;
    }



}

