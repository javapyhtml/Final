# Final Project

## 1. 프로젝트 개요

### 프로젝트 이름  
**Watermelon**

### 프로젝트 설명  
**프로젝트 선정 이유**  
- 지금까지 배운 수업 내용을 기반으로 HTML, CSS, JavaScript를 활용한 프로젝트  
- 회원가입 서비스와 게시판 서비스 기반의 웹사이트 필요  
- 시각적인 효과가 두드러지는 페이지 제작 목표  

**프로젝트 의도 및 목표**  
- **의도**: 배운 내용 복습 및 파이널 프로젝트 준비  
- **목표**: 기존 코드 복습 및 필요한 기능 직접 구현하여 코드 이해도 향상  

**사례 조사 및 주제 선정 이유**  
- 기존 음악 웹사이트들은 단순히 음악 정보 제공 및 감상에 중점  
- 노래 및 아티스트에 대한 사용자 의견 교환의 어려움 존재  

### 기대 효과  
- 사용자 편의성 증대: 번거로운 사이트 이동 불필요  
- 개인화된 플레이리스트 제작 및 공유 가능  
- 실시간 차트 및 장르별 인기 차트 확인 가능  

---

## 2. 팀원 및 팀 소개

| 이수연 | 이시연 | 유기민 | 조다은 | 김태우 |
|:------:|:------:|:------:|:------:|:------:|
| <img src="https://i.pinimg.com/736x/bd/05/6e/bd056e0ff7138b992464d96dfffe8ff7.jpg" alt="이수연" width="150" height="150"> | <img src="https://i.pinimg.com/enabled_lo_mid/736x/7e/6b/39/7e6b39d071c5a0dc33fd2d61788b964d.jpg" alt="이시연" width="150" height="150"> | <img src="https://i.pinimg.com/736x/61/4d/64/614d64378dda207992c4c70d29452b84.jpg" alt="유기민" width="150" height="150"> | <img src="https://i.pinimg.com/736x/13/ad/a1/13ada1a3dbb6c57f8fec1c386ea31546.jpg" alt="김태우" width="150" height="150"> |
| FE | FE | PL | FE | FE |
| [GitHub](https://github.com/javapyhtml) |  |  | [GitHub](https://github.com/su-9woo) |

---

## 3. 주요 기능

### 사용자 관련 기능  
- **회원가입**: 사용자 정보를 DB에 등록  
- **로그인/로그아웃**: 인증 정보를 활용한 로그인  

### 음악 관련 기능  
- **음악 실행**:  
  - 40초간 미리듣기 제공  
  - 앨범 사진, 노래 이름, 아티스트 표시  
  - 음량 조절 가능  
  - 최근 재생목록 버튼으로 이전 기록 확인  

- **음악/아티스트 검색**:  
  - 음악 검색: 장르, 발매일, 조회수, 좋아요 정보 표시  
  - 아티스트 검색: 이름, 그룹/솔로 여부, 데뷔일, 소속사 정보 제공  

- **플레이리스트 기능**:  
  - 사용자 개인화 플레이리스트 생성  

### 게시판 기능  
- 자유게시판, 아티스트 추천, 노래 추천 게시글 기능  
- 게시글에 URL 첨부 및 파일 다운로드 지원  

### 홈페이지 주요 기능  
- 장르별 차트, 노래 TOP 차트, 아티스트별 차트 제공  
- 좋아요 기반 추천 기능  
- 실시간 차트 확인  

---

## 4. 작업 및 역할 분담

| 이름 | 역할 | 담당 작업 |
|-----------------|-----------------|-----------------|
| **이수연** | 검색, 인덱스 | 검색, 헤더/푸터 제작 및 CSS |
| **이시연** | UI 디자인 | 플레이리스트, 로그인, 회원가입 UI 디자인 |
| **유기민** | 프로젝트 총괄 | 전반적인 프로젝트 관리 |
| **조다은** | 데이터 관리 | 마이 프로필 페이지 및 DB 연동 |
| **김태우** | 게시판 개발 | 게시글 및 댓글 관리 페이지 개발 |

---

## 5. 기술 스택

### 5.1 Language  
- **HTML5**  
- **CSS3**  
- **JavaScript**

### 5.2 Frontend  
- IntelliJ IDEA  

### 5.3 Backend  
- Java (Spring Boot)  
- Oracle Database  


### 5.4 기타  
- Python (웹 크롤링)  
- AJAX, jQuery  

---

## 6. 벤치마킹 사례

- **플로 (FLO)**: 전체적인 UI 디자인 참고  
- **지니 (Genie)**: 아티스트 소개 페이지 참고  
- **멜론 (Melon)**: 노래 소개 UI 및 웹 크롤링 활용  
- **스포티파이 (Spotify)**: 다크 모드 UI 참고  

---

## 7. 프로젝트 구조

```plaintext
# 6. Project Structure (프로젝트 구조)
project/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com.example.project/    # 패키지 디렉토리
│   │   │   │   ├── controllers/         # 컨트롤러 클래스
│   │   │   │   │   ├── BasicController.java       // 기본 기능 컨트롤러
│   │   │   │   │   ├── BoardController.java       // 게시판 관련 컨트롤러
│   │   │   │   │   ├── CommentController.java     // 댓글 관리 컨트롤러
│   │   │   │   │   ├── PageMoveController.java    // 페이지 이동 처리
│   │   │   │   │   ├── PlaylistController.java    // 재생목록 관리 컨트롤러
│   │   │   │   │   ├── RestfulController.java     // RESTful API 처리
│   │   │   │   │   ├── SearchController.java      // 검색 요청 처리
│   │   │   │   │   ├── UrlRedirectController.java // URL 리다이렉션 처리
│   │   │   │   │   ├── UserController.java        // 사용자 관리 컨트롤러
│   │   │   │   ├── dao/                  # 데이터 접근 계층
│   │   │   │   │   ├── ArtistDetailDAO.java       // 아티스트 데이터 접근 클래스
│   │   │   │   │   ├── BoardRepository.java       // 게시판 데이터 저장소
│   │   │   │   │   ├── CommentRepository.java     // 댓글 데이터 저장소
│   │   │   │   │   ├── MusicDetailDAO.java        // 음악 세부정보 접근 클래스
│   │   │   │   │   ├── PlaylistDAO.java           // 재생목록 데이터 접근 클래스
│   │   │   │   │   ├── PlaylistInfoDAO.java       // 재생목록 정보 데이터 접근
│   │   │   │   │   ├── UserDAO.java               // 사용자 데이터 접근 클래스
│   │   │   │   ├── dto/                  # 데이터 전송 객체 및 엔티티
│   │   │   │   │   ├── ArtistDetailDTO.java       // 아티스트 상세 데이터 전송 객체
│   │   │   │   │   ├── ArtistDetailEntity.java    // 아티스트 상세 데이터 엔티티
│   │   │   │   │   ├── BoardDTO.java              // 게시판 데이터 전송 객체
│   │   │   │   │   ├── BoardEntity.java           // 게시판 데이터 엔티티
│   │   │   │   │   ├── CommentDTO.java            // 댓글 데이터 전송 객체
│   │   │   │   │   ├── CommentEntity.java         // 댓글 데이터 엔티티
│   │   │   │   │   ├── MusicDetailDTO.java        // 음악 세부 데이터 전송 객체
│   │   │   │   │   ├── MusicDetailEntity.java     // 음악 세부 데이터 엔티티
│   │   │   │   │   ├── PlaylistDTO.java           // 재생목록 데이터 전송 객체
│   │   │   │   │   ├── PlaylistEntity.java        // 재생목록 데이터 엔티티
│   │   │   │   │   ├── PlaylistInfoDTO.java       // 재생목록 정보 데이터 전송 객체
│   │   │   │   │   ├── PlaylistInfoEntity.java    // 재생목록 정보 데이터 엔티티
│   │   │   │   │   ├── SearchDTO.java             // 검색 데이터 전송 객체
│   │   │   │   │   ├── UserDTO.java               // 사용자 데이터 전송 객체
│   │   │   │   │   ├── UserEntity.java            // 사용자 데이터 엔티티
│   │   │   │   ├── services/             # 서비스 계층
│   │   │   │   │   ├── ArtistService.java         // 아티스트 관련 서비스
│   │   │   │   │   ├── BoardService.java          // 게시판 관련 서비스
│   │   │   │   │   ├── CommentService.java        // 댓글 관리 서비스
│   │   │   │   │   ├── MusicService.java          // 음악 관리 서비스
│   │   │   │   │   ├── PlaylistService.java       // 재생목록 관리 서비스
│   │   │   │   │   ├── SearchService.java         // 검색 서비스
│   │   │   │   │   ├── UserService.java           // 사용자 관리 서비스
│   │   │   ├── resources/
│   │   │   │   ├── application.properties         // 환경 설정 파일
│   │   │   │   ├── static/                        # 정적 파일(css, js 등)
│   │   │   │   ├── templates/                     # HTML 템플릿 파일
│   │   │   │   │   ├── boardheader.html
│   │   │   │   │   ├── footer.html
│   │   │   │   │   ├── header.html
│   │   │   │   │   ├── index.html
│   │   │   │   │   ├── playlist.html
│   │   │   │   │   ├── test.html
│   │   │   │   │   ├── board/                     # 게시판 관련 템플릿
│   │   │   │   │   │   ├── list.html
│   │   │   │   │   │   ├── view.html
│   │   │   │   │   │   ├── write.html
│   │   │   │   │   ├── music/                     # 음악 관련 템플릿
│   │   │   │   │   │   ├── artistinfo.html
│   │   │   │   │   │   ├── playlistinfo.html
│   │   │   │   │   ├── top/                       # 장르별 TOP 50
│   │   │   │   │   │   ├── balledtop50.html
│   │   │   │   │   │   ├── bandtop50.html
│   │   │   │   │   │   ├── dancetop50.html
│   │   │   │   │   │   ├── hiphoptop50.html
│   │   │   │   │   │   ├── indietop50.html
│   │   │   │   │   │   ├── poptop50.html
│   │   │   │   │   │   ├── rbtop50.html
│   │   │   │   │   │   ├── trotop50.html
│   │   │   │   │   ├── user/                      # 사용자 관련 템플릿
│   │   │   │   │   │   ├── join.html
│   │   │   │   │   │   ├── login.html
│   │   │   │   │   │   ├── modlogin.html
│   │   │   │   │   │   ├── mypage.html
├── .gitignore                   # Git 무시 파일 목록
├── README.md                    # 프로젝트 개요 및 사용법


