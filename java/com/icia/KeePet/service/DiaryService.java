package com.icia.rmate.service;

import com.icia.rmate.dto.DiaryDTO;
import com.icia.rmate.dto.DiaryEntity;
import com.icia.rmate.dao.DiaryRepository;
import com.icia.rmate.dto.PetEntity;
import com.icia.rmate.dto.RevEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DiaryService {

    @Autowired
    private DiaryRepository diaryRepository;

    private ModelAndView mav;

    Path path = Paths.get(System.getProperty("user.dir"), "src/main/resources/static/upload");

    @Autowired
    private PetService petService;

    // 일지 작성
    public ModelAndView DiaryWrite(DiaryDTO diary) {
        mav = new ModelAndView();

        // 파일 처리: 리뷰 파일이 있는지 확인하고 저장
        MultipartFile petPlayProfile = diary.getPetPlayProfile();
        String savePath = "";

        // 파일이 존재하면 저장
        if (petPlayProfile != null && !petPlayProfile.isEmpty()) {
            String uuid = UUID.randomUUID().toString().substring(0, 8);
            String fileName = petPlayProfile.getOriginalFilename();
            String petPlayProfileName = uuid + "_" + fileName;

            // 리뷰 파일 이름 설정
            diary.setPetPlayProfileName(petPlayProfileName);
            savePath = path + "\\" + petPlayProfileName;  // 파일 저장 경로

            try {
                petPlayProfile.transferTo(new File(savePath));  // 파일 저장
            } catch (IOException e) {
                throw new RuntimeException(e);  // 파일 저장 실패 시 예외 처리
            }
        } else {
            diary.setPetPlayProfileName("default.jpg");  // 기본 이미지 설정
        }

        try {
            // 다이어리 DTO를 엔티티로 변환
            DiaryEntity entity = DiaryEntity.toEntity(diary);
            System.out.println("엔티티 저장 전 확인: " + entity);

            // 리뷰 엔티티 바로 저장
            diaryRepository.save(entity);

        } catch (Exception e) {
            System.out.println("리뷰 등록 실패! 오류: " + e.getMessage());
            mav.setViewName("error");  // 오류 처리 페이지로 리디렉션
            return mav;
        }

        // 리뷰 목록 페이지로 리디렉션
        mav.setViewName("redirect:/list");
        return mav;
    }

    // 특정 사용자의 모든 돌봄 일지를 가져오기
    public List<DiaryDTO> getDiariesByUserId(String userId) {

        // 사용자의 일지 목록을 DB에서 가져오기
        List<DiaryEntity> diaries = diaryRepository.findByUserId(userId);

        // DiaryEntity를 DiaryDTO로 변환하고, petName을 포함시켜 반환
        List<DiaryDTO> diaryDTOs = new ArrayList<>();
        for (DiaryEntity diary : diaries) {
            // petNum을 사용하여 PetEntity 가져오기
            PetEntity pet = petService.getPetNameByPetNum(diary.getPetNum());

            // pet이 null이 아니면 petName을 DTO에 추가
            if (pet != null) {
                DiaryDTO dto = new DiaryDTO();
                dto.setDNum(diary.getDNum());
                dto.setBNum(diary.getBNum());
                dto.setUserId(diary.getUserId());
                dto.setMeal(diary.getMeal());
                dto.setSnack(diary.getSnack());
                dto.setWalk(diary.getWalk());
                dto.setPlay(diary.getPlay());
                dto.setToilet(diary.getToilet());
                dto.setSleep(diary.getSleep());
                dto.setHospital(diary.getHospital());
                dto.setBath(diary.getBath());
                dto.setReservationDate(diary.getBoard().getReservationDate());
                dto.setPetName(diary.getPet().getPetName());
                dto.setPetPlayProfileName(diary.getPetPlayProfileName());

                diaryDTOs.add(dto);
            }
        }
        return diaryDTOs;  // 반환
    }



//    // 일지 수정
//    public String editDiary(int id, DiaryDTO diaryDTO) {
//        Optional<DiaryEntity> existingDiary = diaryRepository.findById(id);
//        if (existingDiary.isPresent()) {
//            DiaryEntity diaryEntity = existingDiary.get();
//            diaryEntity.setMeal(diaryDTO.getMeal());
//            diaryEntity.setSnack(diaryDTO.getSnack());
//            diaryEntity.setPlay(diaryDTO.getPlay());
//            diaryEntity.setToilet(diaryDTO.getToilet());
//            diaryEntity.setSleep(diaryDTO.getSleep());
//            diaryEntity.setWalk(diaryDTO.getWalk());
//            diaryEntity.setHospital(diaryDTO.getHospital());
//            diaryEntity.setBath(diaryDTO.getBath());
//            diaryEntity.setPFileName(diaryDTO.getPFileName());
//            diaryRepository.save(diaryEntity);
//            return "일지가 성공적으로 수정되었습니다.";
//        } else {
//            return "일지를 찾을 수 없습니다.";
//        }
//    }
//
//    // 일지 삭제
//    public String deleteDiary(int id) {
//        Optional<DiaryEntity> existingDiary = diaryRepository.findById(id);
//        if (existingDiary.isPresent()) {
//            diaryRepository.delete(existingDiary.get());
//            return "일지가 성공적으로 삭제되었습니다.";
//        } else {
//            return "일지를 찾을 수 없습니다.";
//        }
//    }
//
//    // 특정 일지 조회
//    public DiaryDTO getDiary(int id) {
//        Optional<DiaryEntity> diaryEntity = diaryRepository.findById(id);
//        return diaryEntity.map(DiaryDTO::toDTO).orElseThrow(() -> new RuntimeException("일지를 찾을 수 없습니다."));
//    }
}