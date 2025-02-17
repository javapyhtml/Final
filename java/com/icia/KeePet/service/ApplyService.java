package com.icia.rmate.service;

import com.icia.rmate.dao.ApplyRepository;
import com.icia.rmate.dao.BoardRepository;
import com.icia.rmate.dto.ApplyDTO;
import com.icia.rmate.dto.ApplyEntity;
import com.icia.rmate.dto.BoardEntity;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.icia.rmate.dto.ApplyDTO.toDTO;

@Service
@RequiredArgsConstructor
public class ApplyService {
    private final BoardRepository brepo;
    private final ApplyRepository applyRepository;
    private final HttpSession session;

    public boolean isAlreadyApplied(int bNum, String loginId) {
        Integer result = applyRepository.existsByBNumAndUserId(bNum, loginId);
        return result != null && result > 0; // 결과가 1이면 true, 0이면 false
    }


    // ApplyDTO를 받아서 ApplyEntity로 변환 후 저장
    public ApplyEntity saveApply(ApplyDTO applyDTO) {
        ApplyEntity applyEntity = ApplyEntity.toEntity(applyDTO);
        System.out.println(applyEntity);
        return applyRepository.save(applyEntity);
    }

    public List<ApplyDTO> getMyApplicants() {
        String userId = (String) session.getAttribute("loginId");
        System.out.println("userId : " + userId);

        if (userId == null) {
            System.out.println("userId가 null입니다.");
            return Collections.emptyList();
        }
        if (userId.isEmpty()) {
            System.out.println("userId가 비어있습니다.");
            return Collections.emptyList();
        }
        try {
            List<BoardEntity> myBoards = brepo.findByUserIdQuery(userId);
            if (myBoards.isEmpty()) {
                System.out.println("내 게시글이 없습니다.");
                return Collections.emptyList();
            }
            List<Integer> bNumList = myBoards.stream()
                    .map(BoardEntity::getBNum)
                    .collect(Collectors.toList());

            for (BoardEntity board : myBoards) {
                System.out.println("게시글 번호 : " + board.getBNum() + ", 게시글 제목 : " + board.getBTitle());
            }

            List<ApplyEntity> myApplicants = applyRepository.findByBNumIn(bNumList);

            if (myApplicants.isEmpty()) {
                System.out.println("신청자가 없습니다.");
                return Collections.emptyList();
            }
            for (ApplyEntity apply : myApplicants) {
                System.out.println("신청자 번호 : " + apply.getANum() + ", 신청자 아이디 : " + apply.getUserId());
            }
            return myApplicants.stream()
                    .map(ApplyDTO::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public void increaseAStatus(int aNum) {
        ApplyEntity applyEntity = applyRepository.findById(aNum).orElseThrow(() -> new IllegalArgumentException("해당 신청을 찾을 수 없습니다."));
//        applyEntity.setAStatus(1);
        applyRepository.save(applyEntity);
    }

    public List<ApplyDTO> getMyAppliedBoards() {
        String userId = (String) session.getAttribute("loginId");
        System.out.println("userId : " + userId);

        if (userId == null) {
            System.out.println("userId가 null입니다.");
            return Collections.emptyList();
        }
        if (userId.isEmpty()) {
            System.out.println("userId가 비어있습니다.");
            return Collections.emptyList();
        }
        try {
            List<ApplyEntity> myApplies = applyRepository.findByUserId(userId);
            if (myApplies.isEmpty()) {
                System.out.println("신청한 게시글이 없습니다.");
                return Collections.emptyList();
            }
            for (ApplyEntity apply : myApplies) {
                System.out.println("신청자 번호 : " + apply.getANum() + ", 게시글 번호 : " + apply.getBNum() +"리뷰작성여부 "+apply.getRStatus());
            }
            return myApplies.stream()
                    .map(ApplyDTO::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }




    public List<ApplyDTO> getApplyListByBNum(int bNum) {
        System.out.println("getApplyListByBNum 호출, bNum: " + bNum);
        try {
            List<ApplyEntity> applyEntities = applyRepository.findByBNumAndAStatusOne(bNum); // 변경: findByBNum 사용
            if (applyEntities.isEmpty()) {
                System.out.println("해당 게시글에 신청한 사람이 없습니다. bNum: " + bNum);
                return Collections.emptyList();
            }
            for (ApplyEntity apply : applyEntities) {
                System.out.println("신청자 번호 : " + apply.getANum() + ", 게시글 번호 : " + apply.getBNum());
            }
            return applyEntities.stream()
                    .map(ApplyDTO::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("getApplyListByBNum 오류 발생 bNum: " + bNum);
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


//    public List<ApplyDTO> checkApplyStatus(int bNum) {
//        System.out.println(" checkApplyStatus 호출, bNum: " + bNum);
//        try {
//            List<ApplyEntity> applyEntities = applyRepository.findByBNumAndAStatus(bNum); // 변경: findByBNum 사용
//            if (applyEntities.isEmpty()) {
//                System.out.println("해당 게시글에 신청한 사람이 없습니다. bNum: " + bNum);
//                return Collections.emptyList();
//            }
//            for (ApplyEntity apply : applyEntities) {
//                System.out.println("신청자 번호 : " + apply.getANum() + ", 게시글 번호 : " + apply.getBNum());
//            }
//            return applyEntities.stream()
//                    .map(ApplyDTO::toDTO)
//                    .collect(Collectors.toList());
//        } catch (Exception e) {
//            System.err.println("getApplyListByBNum 오류 발생 bNum: " + bNum);
//            e.printStackTrace();
//            return Collections.emptyList();
//        }
//    }



    public List<ApplyDTO> getAllAppliedBoards() {
        try {
            List<ApplyEntity> allApplies = applyRepository.findAll();
            if (allApplies.isEmpty()) {
                System.out.println("신청한 게시글이 없습니다.");
                return Collections.emptyList();
            }
            // 모든 신청 내역을 DTO로 변환하여 반환
            return allApplies.stream()
                    .map(ApplyDTO::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


}
