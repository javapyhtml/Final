package com.icia.rmate.service;

import com.icia.rmate.dao.BoardRepository;
import com.icia.rmate.dao.PetRepository;
import com.icia.rmate.dao.UserInfoRepository;
import com.icia.rmate.dto.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository brepo;
    public ModelAndView reportBoard;
    private final HttpSession session;
    private final HttpServletRequest request;
    private final HttpServletResponse response;


    // 저장 경로
    Path path = Paths.get(System.getProperty("user.dir"), "src/main/resources/static/boardUpload");

    private static ModelAndView mav;

    public List<BoardDTO> getMyBoards() {
        String userId = (String) session.getAttribute("loginId");
        System.out.println("userId : " + userId);

        if (userId == null) {
            System.out.println("userId가 null입니다.");
            return null;
        }
        if (userId.isEmpty()) {
            System.out.println("userId가 비어있습니다.");
            return null;
        }
        try {
            List<BoardEntity> result = brepo.findByUserIdQuery(userId);
            if (result == null) {
                System.out.println("findByUserId 결과가 null 입니다.");
                return null;
            }
            // System.out.println(result); // toString() 호출 제거
            for (BoardEntity board : result) {
                System.out.println("게시글 번호 :" + board.getBNum() + ", 게시글 제목 :" + board.getBTitle()); // 필요한 정보만 로그로 출력
            }

            return result.stream()
                    .map(BoardDTO::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    public String boardToHistory(int bNum) {
//
//       brepo.boardToHistory(bNum);
//       // 완료 메시지 반환
//        return "즐거운 여행 되셧나요? 리뷰를 작성해보세요!";
//    }


//    public int getCategoryNum(int bNum) {
//        BoardEntity boardEntity = brepo.findById(bNum)
//                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
//        return boardEntity.getCategoryNum();
//    }

//    public List<Object[]> boardList(@ModelAttribute SearchOptionDTO searchOptionDTO) {
//        // SearchOptionDTO에서 값 추출
//        String keyword = searchOptionDTO.getKeyword();
//
//        return brepo.searchBoardsWithJoin(keyword);
//    }

    public List<Object[]> boardList(@ModelAttribute SearchOptionDTO searchOptionDTO, int petStatus) {
        String keyword = searchOptionDTO.getKeyword();
        return brepo.searchBoardsWithPetStatus(keyword, petStatus);
    }

    @Autowired
    private UserInfoRepository urepo;

    @Autowired
    private PetRepository prepo;

    public void saveBoard(BoardDTO board) {

        mav = new ModelAndView();
        MultipartFile bProfile = board.getBFile();


        if (bProfile != null && !bProfile.isEmpty()) {

            String uuid = UUID.randomUUID().toString().substring(0,8);
            String fileName = bProfile.getOriginalFilename();
            String newFileName = uuid + "_" + fileName;

            // 파일 저장 경로 설정

            board.setBFileName(newFileName);
            String savePath = path + "\\" + newFileName;
            try {
                bProfile.transferTo(new File(savePath));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }else if(board.getBFileName() == null){
            board.setBFileName("default.png"); // 기본 이미지
        }

        // 1. BoardDTO -> BoardEntity로 변환
        BoardEntity entity = BoardEntity.toEntity(board);
        System.out.println("엔티티 저장전 확인"+entity);

        // 2. UserId로 UserInfoEntity 가져오기
        UserInfoEntity userInfoEntity = urepo.findByUserId(board.getUserId());
        if (userInfoEntity != null) {
            entity.setUserInfo(userInfoEntity);

            // 3. 보호자 주소 설정
            String guardianAddress = userInfoEntity.getUserAddress();
            entity.setGuardianAddress(guardianAddress);  // 보호자 주소 설정
        }

        // 4. PetNum으로 PetEntity 가져오기
        PetEntity petEntity = prepo.findById(board.getPetNum()).orElse(null);
        if (petEntity != null) {
            entity.setPet(petEntity);
        }

        // 5. BServices 처리: 쉼표로 구분된 문자열을 그대로 저장
        String bServices = board.getBServices();
        if (bServices != null && !bServices.isEmpty()) {
            entity.setBServices(bServices);  // 다중 선택된 서비스 값 저장 (쉼표로 구분된 값)
        }

        // 6. entity 저장
        brepo.save(entity);
    }


    public ModelAndView getBoardDetail(int bnum) {
        System.out.println("[2] controller → service : " + bnum);
        mav = new ModelAndView();

        Optional<BoardEntity> entity = brepo.findById(bnum);
        if(entity.isPresent()){
            BoardDTO dto = BoardDTO.toDTO(entity.get());
            mav.addObject("view", dto);
            mav.setViewName("/Board/boarddetail");
        } else {
            mav.setViewName("redirect:/board1Form");
        }

        return mav;
    }

    public BoardDTO findById(int bNum) {
        BoardEntity boardEntity = brepo.findByBNum(bNum);
        // 게시글 없을 경우 에러 발생
        if(boardEntity == null){
            throw new RuntimeException("해당 게시글이 존재하지 않습니다. 게시글 번호 : "+ bNum);
        }
        return BoardDTO.toDTO(boardEntity);
    }




    // Entity를 DTO로 변환하는 메소드
    private BoardDTO convertToDTO(BoardEntity boardEntity) {
        BoardDTO boardDTO = new BoardDTO();
        // 필요한 필드만 매핑
        boardDTO.setBNum(boardEntity.getBNum());
        boardDTO.setBTitle(boardEntity.getBTitle());
        boardDTO.setBContents(boardEntity.getBContents());
        boardDTO.setUserId(boardEntity.getUserId());
        boardDTO.setBDate(boardEntity.getBDate());
        boardDTO.setBUpdate(boardEntity.getBUpdate());
        boardDTO.setBContents(boardEntity.getBContents());
        boardDTO.setBFileName(boardEntity.getBFileName());
        boardDTO.setGuardianName(boardEntity.getGuardianName());
        boardDTO.setGuardianPhone(boardEntity.getGuardianPhone());
        boardDTO.setGuardianAddress(boardEntity.getGuardianAddress());
        boardDTO.setBServices(boardEntity.getBServices());
        boardDTO.setBTime(boardEntity.getBTime());
        boardDTO.setReservationDate(boardEntity.getReservationDate());
        boardDTO.setUserId(boardEntity.getUserId());
        boardDTO.setPetNum(boardEntity.getPetNum());


        return boardDTO;
    }

    // 게시글 번호(BNum)로 게시글 조회
    public BoardDTO getBoardByBNum(int bNum) {
        BoardEntity boardEntity = brepo.findById(bNum)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        return BoardDTO.toDTO(boardEntity);  // 게시글 데이터를 DTO로 변환하여 반환
    }

    // 모든 게시글을 가져오되, Entity -> DTO로 변환
    public List<BoardDTO> getAllBoards() {
        List<BoardEntity> boardEntities = brepo.findAll(); // Entity 목록 가져옴

        List<BoardDTO> boardDTOs = new ArrayList<>();
        for (BoardEntity boardEntity : boardEntities) {
            boardDTOs.add(convertToDTO(boardEntity)); // 각 Entity를 DTO로 변환하여 추가
        }

        return boardDTOs;
    }

    // 모든 게시글을 가져오되, Entity -> DTO로 변환 (서비스 번호 5번 제외)
    public List<BoardDTO> getAllBoardListsWithoutService5() {
        List<BoardEntity> boardEntities = brepo.findAll(); // 모든 게시글을 가져옴

        List<BoardDTO> boardDTOs = new ArrayList<>();
        for (BoardEntity boardEntity : boardEntities) {
            // BServices에 5번이 포함되어 있지 않은 게시글만 가져옴
            if (boardEntity.getBServices() != null && !boardEntity.getBServices().contains("5")) {
                // petNum을 이용하여 Pet 정보 가져오기
                PetEntity pet = prepo.findById(boardEntity.getPetNum()).orElse(null);
                String petName = (pet != null) ? pet.getPetName() : "알 수 없음"; // petName을 찾을 수 없으면 '알 수 없음'

                // BoardEntity를 BoardDTO로 변환
                BoardDTO dto = BoardDTO.toDTO(boardEntity);
                dto.setPetName(petName);  // petName을 DTO에 설정
                boardDTOs.add(dto);  // DTO 리스트에 추가
            }
        }
        return boardDTOs;  // 변환된 BoardDTO 리스트 반환
    }

    // 서비스 번호 5번만 고른 게시글만 가져오기
    public List<BoardDTO> getAllBoardsWithService5() {
        List<BoardEntity> boardEntities = brepo.findAll(); // Entity 목록 가져옴

        List<BoardDTO> boardDTOs = new ArrayList<>();
        for (BoardEntity boardEntity : boardEntities) {
            // BServices에 5번이 포함되어 있으면 DTO로 변환하여 추가
            if (boardEntity.getBServices() != null && boardEntity.getBServices().contains("5")) {
                boardDTOs.add(convertToDTO(boardEntity)); // 각 Entity를 DTO로 변환하여 추가
            }
        }

        return boardDTOs;
    }



    public void updateBoard(BoardDTO board) {
        BoardEntity boardEntity = brepo.findById(board.getBNum()).orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        // BDate가 이미 null인 경우, 수정하지 않도록 처리
        if (boardEntity.getBDate() == null) {
            boardEntity.setBDate(LocalDateTime.now()); // 게시글 최초 작성 시 날짜를 설정
        }

        // BUpdate는 수정 시간
        boardEntity.setBUpdate(LocalDateTime.now());
        boardEntity.setBContents(board.getBContents());
        boardEntity.setBFileName(board.getBFileName());
        boardEntity.setBServices(board.getBServices());
        boardEntity.setGuardianPhone(board.getGuardianPhone());
        boardEntity.setGuardianAddress(board.getGuardianAddress());
        boardEntity.setReservationDate(board.getReservationDate());

        // 수정된 엔티티 저장
        brepo.save(boardEntity);
    }


    public BoardEntity findBoardById(int bNum) {
        return brepo.findByBNum(bNum);
    }


    public List<BoardEntity> getBoardsByBServices(String bServices) {
        // Fetch boards from the repository where BServices = "5"
        return brepo.findByBServices(bServices);
    }



}
