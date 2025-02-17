package com.icia.rmate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icia.rmate.dao.ApplyRepository;
import com.icia.rmate.dao.BoardRepository;
import com.icia.rmate.dao.CommentRepository;
import com.icia.rmate.dao.CourseRepository;
import com.icia.rmate.dto.*;
import com.icia.rmate.service.*;
import com.icia.rmate.util.JsonResult;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;  // Model import 추가
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class BoardController {
    private final CommentService csvc; // CommentService 주입
    private final HttpSession session;
    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepository course;
    private final BoardService bsvc;
    @Autowired
    private BoardRepository brepo;
    @Autowired
    private CommentRepository crepo;
    @Autowired
    private PetService psvc;


    @Autowired
    private ApplyRepository arepo;
    @Autowired
    private CourseRepository courseRepository;

    @GetMapping("/board1Form")
    public String board1Form() {
        return "Board/board1";
    }

    @GetMapping("/serviceForm")
    public String board2Form() {
        return "Board/service";
    }

    @GetMapping("/aboutForm")
    public String board3Form() {
        return "Board/about";
    }

    @GetMapping("/questionForm")
    public String board4Form() {
        return "Board/question";
    }



    @GetMapping("/puppyEx")
    public String index1Form() {
        return "puppy";
    }

    @GetMapping("/smallEx")
    public String index2Form() {
        return "small";
    }

    @GetMapping("/mediumEx")
    public String index3Form() {
        return "medium";
    }

    @GetMapping("/largeEx")
    public String index4Form() {
        return "large";
    }

//    @GetMapping("/bWriteForm")
//    public String boardformForm() {
//        return "Board/boardform";
//    }

    @Autowired
    private UserService usvc;

//    // 유저 및 반려견 정보 가져오는 메소드
//    @GetMapping("/bWriteForm")
//    public String bWriteForm(HttpSession session, Model model) {
//        String userId = (String) session.getAttribute("loginId"); // 세션에서 유저 아이디 가져오기
//        System.out.println("Session에서 가져온 userId: " + userId);  // userId 확인
//
//        // 유저 정보 가져오기
//        UserInfoDTO user = usvc.getUserInfo(userId);
//        System.out.println("회원주소: " + user.getUserAddress());
//
//        // 반려견 정보 가져오기 (UserId 기준)
//        List<PetDTO> pets = psvc.getPetsByUserId(userId);
//        System.out.println("반려견 목록: " + pets); // pets 목록 확인
//
//        model.addAttribute("user", user); // 유저 정보 전달
//        model.addAttribute("pets", pets);   // 반려견 목록 전달
//        model.addAttribute("board", new BoardDTO()); // 빈 BoardDTO 객체 전달
//        return "/Board/boardform";  // 작성 페이지로 이동
//    }

    @GetMapping("/bWriteForm")
    public @ResponseBody Map<String, Object> bWriteForm(HttpSession session) {
        String userId = (String) session.getAttribute("loginId");  // 세션에서 유저 아이디 가져오기
        System.out.println("Session에서 가져온 userId: " + userId);  // userId 확인

        // 유저 정보 가져오기
        UserInfoDTO user = usvc.getUserInfo(userId);
        System.out.println("회원주소: " + user.getUserAddress());

        // 반려견 정보 가져오기 (UserId 기준)
        List<PetDTO> pets = psvc.getPetsByUserId(userId);  // 반려견 목록 가져오기
        System.out.println("반려견 목록: " + pets); // pets 목록 확인

        // pets를 JSON 형태로 반환
        Map<String, Object> response = new HashMap<>();
        response.put("pets", pets);
        return response;
    }

    // 게시글 작성 메소드
    @PostMapping("/bWrite")
    public String bWrite(@ModelAttribute BoardDTO board, HttpSession session, @RequestParam(value = "BFile", required = false) MultipartFile file) {
        String userId = (String) session.getAttribute("loginId");
        board.setUserId(userId);
        System.out.println("bWrite에서 가져온 petStatus: " + board.getPetStatus());

        bsvc.saveBoard(board);

        // petStatus에 따라 리디렉션 경로 설정
        return (board.getPetStatus() == 0) ? "redirect:/board1Form" : "redirect:/cat/board1Form";
    }

    // 게시글 상세보기
//    @GetMapping("/boarddetail/{bNum}")
//    public String findById(@PathVariable int bNum, Model model) {
//        BoardDTO boardDTO = bsvc.findById(bNum);
//        model.addAttribute("board", boardDTO);
//        return "Board/boarddetail"; // 게시글 상세 페이지 html 파일 경로
//    }

    @GetMapping("/boarddetail/{bNum}")
    public String boardDetail(@PathVariable int bNum, Model model, HttpSession session) {
        // 게시글 가져오기
        BoardDTO board = bsvc.findById(bNum);

        // 게시글에 저장된 petNum을 사용하여 반려견 정보 가져오기
        PetDTO pet = psvc.getPetByPetNum(board.getPetNum());

        // 모델에 board와 pet 정보를 추가
        model.addAttribute("board", board);
        model.addAttribute("pet", pet);  // 강아지 정보 추가

        // 보드 상세 페이지로 이동
        return "Board/boarddetail";
    }


    @Autowired
    private BoardOptionService boardService;

    @PostMapping("/saveSearchOption")
    public String saveSearchOption(SearchOptionDTO searchOption, HttpSession session) {
        // 기존 세션의 searchOption 객체를 제거 (세션 초기화)
        session.removeAttribute("searchOption");
        System.out.println("서치 dto 작동확인 : " + searchOption);
        // 새로운 searchOption 객체를 세션에 저장
        session.setAttribute("searchOption", searchOption);

        // 저장 후, boardList 페이지로 리다이렉트
        return "redirect:/board1Form";
    }

    @GetMapping("/myboardList")
    public ResponseEntity<List<BoardDTO>> myboardList() {
        List<BoardDTO> myBoards = bsvc.getMyBoards();
        System.out.println(myBoards + ": dto로 변환된 myBoards를 확인하라!");

        if (myBoards == null || myBoards.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(myBoards, HttpStatus.OK);
        }
    }


    @GetMapping("/myCommentList")
    public ResponseEntity<List<CommentDTO>> myCommentList() {
        List<CommentDTO> myComments = csvc.getMyComments(); // 서비스에서 댓글 목록 가져오기
        System.out.println(myComments + ": dto로 변환된 myComments를 확인하라!");


        if (myComments == null || myComments.isEmpty()) { // 댓글이 없으면 NO_CONTENT 상태 코드 반환
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(myComments, HttpStatus.OK);
        }
    }

    @GetMapping("/getAllBoardLists")
    public ResponseEntity<List<BoardDTO>> getAllBoardLists() {
        List<BoardDTO> allBoards = bsvc.getAllBoards(); // 변환된 BoardDTO 리스트를 가져옵니다.
        System.out.println(allBoards + ": 모든 게시글을 확인하라!");

        if (allBoards == null || allBoards.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(allBoards, HttpStatus.OK);
        }
    }

    @GetMapping("/getAllBoardListsWithoutService5")
    public ResponseEntity<List<BoardDTO>> getAllBoardListsWithoutService5() {
        List<BoardDTO> allBoardsWithService5 = bsvc.getAllBoardListsWithoutService5(); // 서비스 번호 5번 제외

        if (allBoardsWithService5 == null || allBoardsWithService5.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 목록이 없으면 204 반환
        } else {
            return new ResponseEntity<>(allBoardsWithService5, HttpStatus.OK); // 목록이 있으면 200 반환
        }
    }

    @GetMapping("/getAllBoardListsWithService5")
    public ResponseEntity<List<BoardDTO>> getAllBoardListsWithService5() {
        List<BoardDTO> allBoardsWithService5 = bsvc.getAllBoardsWithService5(); // 서비스 번호 5번만 고른 게시글 리스트 가져옴

        if (allBoardsWithService5 == null || allBoardsWithService5.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(allBoardsWithService5, HttpStatus.OK);
        }
    }


    @PostMapping("/addCourse")
    @ResponseBody
    public ResponseEntity<JsonResult> addCourse(@RequestBody Map<String, Object> payload) throws IOException {
        // 테이블 행의 수를 확인합니다.
        long courseCount = courseRepository.count();
        if (courseCount >= 15) {
            return new ResponseEntity<>(JsonResult.fail(new RuntimeException("코스수를 더 추가할수 없습니다.")), HttpStatus.CONFLICT); // 409 Conflict 상태 코드 반환
        }

        Map<String, Object> course = (Map<String, Object>) ((List) payload.get("courseList")).get(0);
        String oLink = (String) payload.get("oLink");
        int bNum = Integer.parseInt(payload.get("bNum").toString());
        String userId = (String) payload.get("userId");
        int CStatus = 0;

        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setId(Long.parseLong(course.get("id").toString()));
        courseEntity.setName((String) course.get("name"));
        courseEntity.setAddress((String) course.get("address"));
        courseEntity.setPhone((String) course.get("phone"));
        courseEntity.setX(Double.parseDouble(course.get("x").toString()));
        courseEntity.setY(Double.parseDouble(course.get("y").toString()));
        courseEntity.setRegDt(new ObjectMapper().convertValue(course.get("regDt"), Date.class));
        courseEntity.setModDt(new ObjectMapper().convertValue(course.get("modDt"), Date.class));
        courseEntity.setBNum(bNum);
        courseEntity.setOLink(oLink);
        courseEntity.setUserId(userId);
        courseEntity.setCStatus(CStatus);

        // 주소가 이미 존재하는지 확인
        Optional<CourseEntity> existingCourse = courseRepository.findByAddress(courseEntity.getAddress());

        // 주소가 이미 존재한다면 저장하지 않음
        if (existingCourse.isPresent()) {
            return new ResponseEntity<>(JsonResult.fail(new Exception("이미 동일한 주소가 존재합니다.")), HttpStatus.CONFLICT); // 409 Conflict 상태 코드 반환
        }

        // 존재하지 않으면 저장
        courseService.addCourse(courseEntity);
        return new ResponseEntity<>(new JsonResult().success(), HttpStatus.OK); // 200 OK 상태 코드 반환
    }

    @PostMapping("/bEdit")
    public String bEdit(@ModelAttribute BoardDTO board,
                        HttpSession session,
                        @RequestParam(value = "BFile", required = false) MultipartFile file, Model model) {
        // 세션에서 유저 아이디 가져오기
        String userId = (String) session.getAttribute("loginId");
        board.setUserId(userId);  // UserId 설정

        // 반려견 정보 가져오기 (UserId 기준)
        List<PetDTO> pets = psvc.getPetsByUserId(userId);
        model.addAttribute("pets", pets);   // 반려견 목록 전달

        // 수정된 데이터 출력 (확인용)
        System.out.println("bEdit에서 가져온 userId: " + userId);  // userId 확인
        System.out.println("bEdit에서 가져온 petNum: " + board.getPetNum());  // petNum 확인
        System.out.println("bEdit에서 가져온 BServices: " + board.getBServices()); // 다중 서비스 확인
        System.out.println("bEdit에서 가져온 GuardianPhone: " + board.getGuardianPhone());
        System.out.println("bEdit에서 가져온 GuardianAddress: " + board.getGuardianAddress());
        System.out.println("bEdit에서 가져온 ReservationDate: " + board.getReservationDate());

        // 파일이 있을 경우 처리
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            // 파일 저장 로직 추가
            // 예: 파일을 서버에 저장하고 BoardDTO에 파일 이름을 설정
            board.setBFileName(fileName);
        }


        // 수정 시 수정 날짜(BUpdate) 추가
        board.setBUpdate(LocalDateTime.now());  // 또는 적절한 날짜 형식으로 설정 (예: `new Date()`)


        // 수정된 데이터를 저장
        bsvc.updateBoard(board);

        // 수정 후 해당 게시글 상세 페이지로 리다이렉트 (bNum 포함)
        return "redirect:/boarddetail/" + board.getBNum();  // 수정 후 게시글 상세 페이지로 리다이렉트
    }

    // Endpoint to fetch boards where BServices = "5"
    @GetMapping("/fetch-pickup-boards")
    @ResponseBody
    public List<BoardEntity> getPickupBoards() {
        // Fetch boards from the service where BServices = "5"
        return bsvc.getBoardsByBServices("5");
    }

    @GetMapping("/getPetStatus")
    @ResponseBody
    public Integer getPetStatus(HttpSession session) {
        Integer petStatus = (Integer) session.getAttribute("petStatus");
        return petStatus != null ? petStatus : 0; // 기본값 0 (반려견)
    }

//    @GetMapping("/users")
//    public ResponseEntity<List<UserInfoEntity>> getAllUsers() {
//        return ResponseEntity.ok(usvc.getAllUsers());
//    }

    @GetMapping("/users")
    public ResponseEntity<List<UserInfoDTO>> getAllUsers() {
        return ResponseEntity.ok(usvc.getAllUsers());
    }



}








