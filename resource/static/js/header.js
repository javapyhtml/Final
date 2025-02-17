    window.onload = function() {
      // 페이지가 로드되면 errorMessage div에서 텍스트를 가져오기
      var errorMessage = document.getElementById("errorMessage").textContent;

      // errorMessage가 있을 경우 alert로 띄우기
      if (errorMessage) {
        alert(errorMessage); // 에러 메시지를 표시한 후

        // alert이 끝난 후 비동기적으로 error 메시지를 서버에서 삭제
        setTimeout(function() {
          $.ajax({
            url: "/clearError",  // 서버의 오류 메시지 삭제 API 엔드포인트
            type: "POST",        // 요청 타입
            success: function(response) {
              // 서버에서 성공적으로 오류 메시지가 삭제됨
              console.log("Error message cleared.");
            },
            error: function(xhr, status, error) {
              // 오류 처리
              console.error("Error clearing the error message:", error);
            }
          });
        }, 0); // alert 후 즉시 실행
      }
    };

  window.onload = function() {
    // 페이지가 로드되면 errorMessage div에서 텍스트를 가져오기
    var errorMessage = document.getElementById("errorMessage1").textContent;

    // errorMessage가 있을 경우 alert로 띄우기
    if (errorMessage) {
      alert(errorMessage); // 에러 메시지를 표시한 후

      // alert이 끝난 후 비동기적으로 error 메시지를 서버에서 삭제
      setTimeout(function() {
        $.ajax({
          url: "/clearError",  // 서버의 오류 메시지 삭제 API 엔드포인트
          type: "POST",        // 요청 타입
          success: function(response) {
            // 서버에서 성공적으로 오류 메시지가 삭제됨
            console.log("Error message cleared.");
          },
          error: function(xhr, status, error) {
            // 오류 처리
            console.error("Error clearing the error message:", error);
          }
        });
      }, 0); // alert 후 즉시 실행
    }
  };


  document.getElementById("board1")?.addEventListener("click", function () {
    window.location.href = "/board1Form"; // 마이페이지로 이동
  });


  document.getElementById("board2")?.addEventListener("click", function () {
    window.location.href = "/board1Form"; // 마이페이지로 이동
  });


  document.getElementById("board3")?.addEventListener("click", function () {
    window.location.href = "/board1Form"; // 마이페이지로 이동
  });


  document.getElementById("board4")?.addEventListener("click", function () {
    window.location.href = "/RList"; // 마이페이지로 이동
  });

  // 마이페이지 버튼 클릭 시 /mMyPage 페이지로 이동 (로그인 상태에서만 표시)
  document.getElementById("myPage")?.addEventListener("click", function () {
    window.location.href = "/myPageForm"; // 마이페이지로 이동
  });

  // 로그아웃 버튼 클릭 시 로그아웃 처리
  document.getElementById("logout")?.addEventListener("click", function () {
    window.location.href = "/mLogout"; // 로그아웃 경로로 이동
  });


  $('#loginForm').on('submit', function(event) {
    event.preventDefault();

    const loginData = {
      UserId: $('#User_Id').val(),
      UserPw: $('#User_Pw').val()
    };

  });

  // 로그인 버튼 클릭 시 모달 열기
  document.getElementById("showLoginModal").onclick = function() {
    document.getElementById("loginModal").style.display = "block";
    document.querySelector('.modal-overlay').style.display = 'block';
  };

  // 회원가입 클릭 시 모달 열기(이용약관 후 회원가입)
  document.addEventListener('DOMContentLoaded', function () {
    const termsCheckbox = document.querySelectorAll('.terms-checkbox');
    const privacyCheckbox = document.querySelector('.privacy-checkbox');
    const termAgreeBtn = document.getElementById("term-agree-btn");

    const showJoinModalBtn = document.getElementById("showJoinModal");
    const termModal = document.getElementById('termModal');
    const joinModal = document.getElementById('joinModal');
    const closeTermModal = document.getElementById('closeTermModal');
    const closeJoinModal = document.getElementById('closeJoinModal');
    const modalOverlay = document.querySelector('.modal-overlay');

    const urlParams = new URLSearchParams(window.location.search);
    const showModal = urlParams.get('showModal');

    if (showModal === 'true') {
      joinModal.style.display = "block";  // 회원가입 모달 띄우기
      modalOverlay.style.display = 'block';  // 배경 오버레이 띄우기
    }

    // 약관 동의 모달 열기
    showJoinModalBtn.addEventListener('click', function(e) {
      e.preventDefault();
      termModal.style.display = "block";  // 약관 모달 띄우기
      modalOverlay.style.display = 'block';  // 배경 오버레이 띄우기
    });

    // "약관 동의" 버튼 클릭 시 필수 체크박스가 선택되었는지 확인
    termAgreeBtn.addEventListener('click', function (event) {
      let allChecked = true;

      // 필수 약관 체크박스 확인
      termsCheckbox.forEach(function(checkbox) {
        if (!checkbox.checked) {
          allChecked = false;
        }
      });

      if (!allChecked || !privacyCheckbox.checked) {
        event.preventDefault();  // 체크되지 않으면 모달을 닫지 않음
        alert("모든 필수 약관에 동의해 주세요.");
      } else {
        // 약관 동의가 완료되면 서버에 POST 요청 보내기
        const termForm = document.getElementById('termForm');
        const formData = new FormData(termForm);

        fetch('/mTermAgree', {
          method: 'POST',
          body: formData
        })
                .then(response => response.json())
                .then(data => {
                  if (data.success) {
                    console.log("약관 동의 완료");
                    termModal.classList.remove('show'); // 약관 모달 닫기
                    joinModal.classList.add('show'); // 회원가입 모달 열기
                    modalOverlay.classList.add('show'); // 배경 오버레이 띄우기
                  } else {
                    alert(data.message); // 실패 메시지 처리
                  }
                })
                .catch(error => {
                  console.error('Error:', error);
                });
      }
    });

    // "약관 동의" 모달 닫기
    closeTermModal.addEventListener('click', function() {
      termModal.style.display = 'none';  // 약관 모달 닫기
      modalOverlay.style.display = 'none';  // 배경 오버레이 숨기기
    });

    // "회원가입" 모달 닫기
    closeTermModal.addEventListener('click', function() {
      termModal.style.display = 'none';  // 약관 모달 닫기
      modalOverlay.style.display = 'none';  // 배경 오버레이 숨기기
    });

    // Select All 체크박스 처리
    const $agreementForm = document.querySelector('.termForm');
    const $selectAll = $agreementForm.querySelector('.select-all');
    const $listInput = $agreementForm.querySelectorAll('.list input');
    const $selectAllMkt = $agreementForm.querySelector('.select-all-marketing');
    const $mktListInput = $agreementForm.querySelectorAll('.marketing-check input');

    const toggleCheckbox = (allBox, itemBox) => {
      allBox.addEventListener('change', () => {
        itemBox.forEach((item) => {
          item.checked = allBox.checked;
        });
      });
    }
    toggleCheckbox($selectAll, $listInput);
    toggleCheckbox($selectAllMkt, $mktListInput);

    // 개별 체크박스 체크 시 Select All 체크 처리
    $listInput.forEach((item) => {
      item.addEventListener('change', () => {
        const isChecked = Array.from($listInput).every(i => i.checked);
        $selectAll.checked = isChecked;
      });
    });

    $mktListInput.forEach((item) => {
      item.addEventListener('change', () => {
        const isChecked = Array.from($mktListInput).some(i => i.checked);
        $selectAllMkt.checked = isChecked;
      });
    });
  });

  // 로그인 모달 x 버튼 클릭 시 모달 닫기
  document.getElementById("closeLoginModal").onclick = function() {
    document.getElementById("loginModal").style.display = "none";
    document.querySelector('.modal-overlay').style.display = 'none';
  };

  // 회원가입 모달 x 버튼 클릭 시 모달 닫기
  document.getElementById("closeJoinModal").onclick = function() {
    document.getElementById("joinModal").style.display = "none";
    document.querySelector('.modal-overlay').style.display = 'none';
  };

  // 페이지 로드 시 초기화
  window.onload = function() {
    document.getElementById('GType').value = '';
    document.getElementById('selectedOption').textContent = '';
  };


  // // 페이지 로드가 완료된 후 실행
  // document.addEventListener("DOMContentLoaded", function() {
  //   // 아이디가 "index"인 요소에 클릭 이벤트 리스너 추가
  //   document.getElementById("index").addEventListener("click", function() {
  //     // "index" 페이지로 이동
  //     window.location.href = "/index";
  //   });
  // });

  // 아이디 중복체크
  let user_Id = $('#User_Id');
  let check1 = $('#check1');
  let check_id = false;

  // ajax로 id 가입여부 확인하기
  user_Id.blur(() => { // blur 이벤트로 변경
    if (user_Id.val() === "" || !user_Id.val().match(/[a-zA-Z]+/)) {
      check1.html("아이디는 영어만 입력 가능합니다.");
      check1.css('color', 'red');
      check_id = false;
      return; // Ajax 요청 보내지 않음
    }

    $.ajax({
      type : "POST",
      url : "/idCheck",
      data : { "UserId" : user_Id.val() },
      dataType : "text",
      success : function(result){
        if(result=="OK"){
          check1.html(user_Id.val() + "는 사용 가능한 아이디");
          check1.css('color', 'blue');
          check_id = true;
        } else {
          check1.html(user_Id.val() + "는 사용중인 아이디");
          check1.css('color', 'red');
          check_id = false;
        }
      },
      error : function (){
        alert('idCheck 통신 실패!')
      }
    });
  });

  // 비밀번호 정규화
  let user_Pw = $('#User_Pw');
  let check2 = $('#check2');
  let check_pw1 = false;

  user_Pw.keyup(()=>{
    let pwVal = user_Pw.val();

    // 기본값 : -1
    let num  = pwVal.search(/[0-9]/);        // 숫자
    let eng  = pwVal.search(/[a-z]/);        // 소문자
    let eng1 = pwVal.search(/[A-Z]/);        // 대문자
    let spe  = pwVal.search(/[~!@#$%^&*]/);  // 특수문자
    let spc  = pwVal.search(/\s/);           // 공백

    // 통합
    let checked =  pwVal.search(/^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[~!@#$%^&*])[\S]{8,16}$/);

    if(pwVal.length < 8 || pwVal.length > 16){
      check2.html('8-16자리 입력!');
      check2.css('color', 'red');
      check_pw1 = false;
    } else if(spc != -1) {
      check2.html('공백없이 입력!');
      check2.css('color', 'red');
      check_pw1 = false;
    } else if(num==-1 || eng==-1 || eng1==-1 || spe==-1){
      check2.html('숫자, 특수문자, 대소문자 혼합 입력!');
      check2.css('color', 'red');
      check_pw1 = false;
    } else {
      check2.html('사용가능한 비밀번호!');
      check2.css('color', 'blue');
      check_pw1 = true;
    }

  });

  // 비밀번호 확인
  let checkPw = $('#checkPw');
  let check3 = $('#check3');
  let check_pw2 = false;

  checkPw.keyup(()=>{
    pwVal = user_Pw.val();
    let checkVal = checkPw.val();

    if(pwVal == checkVal){
      check3.html(`비밀번호 일치`);
      check3.css('color', 'blue');
      check_pw2 = true;
    } else {
      check3.html(`비밀번호 불일치`);
      check3.css('color', 'red');
      check_pw2 = false;
    }
  });

  // 이메일 인증하기
  let checkEmail = $('#checkEmail');
  let user_Email = $('#User_Email');
  let check4 = $('#check4');
  let check_email = false;

  checkEmail.click(()=>{
    // ajax 사용해서 이메일 전송 및 인증번호 받아오기
    $.ajax({
      type : "POST",
      url : "/emailCheck",
      data : { "UserEmail" : user_Email.val() },
      dataType: "text",
      success : (uuid)=>{
        console.log(uuid);
        check4.empty();
        check4.append(`<br/><input type="text" id="uuid1" />`);
        check4.append(` <input type="button" value="인증" id="btnUUID" data-value="${uuid}" />`);
      },
      error : ()=>{
        alert('emailCheck 통신 실패');
      }
    });
  });

  // 인증번호 확인하기
  // let btnUUID = $('#btnUUID');
  $(document).on('click', '#btnUUID', function(){
    let uuid = $(this).data("value");

    // 입력한 인증번호
    let uuid1 = $('#uuid1').val();

    if(uuid==uuid1){
      alert('이메일 인증 성공');
      check4.hide();
      user_Email.attr('readonly', true);
      check_email = true;
    } else {
      alert('이메일 인증번호를 확인해주세요');
      $('#uuid1').val("");
      check_email = false;
    }
  });
  // 이름 유효성 검사
  let userName = $('#UserName');
  let checkName = false; // 이름 유효성 검사 결과 변수

  userName.blur(() => {
    let nameVal = userName.val();
    let nameRegExp = /^[가-힣]{2,5}$/; // 2~5자의 한글만 허용하는 정규식

    if (nameVal === "" || !nameRegExp.test(nameVal)) {
      alert("이름은 2~5자의 한글(초성X)만 입력 가능합니다.");
      checkName = false;
      updateSubmitButton(); // 가입 버튼 상태 업데이트 (필요한 경우)
      return;
    }

    checkName = true;
    updateSubmitButton(); // 가입 버튼 상태 업데이트 (필요한 경우)
  });

  // 닉네임 유효성 검사 및 중복 확인
  let userNickname = $('#UserNickname');
  let checkNickname = $('#checkNickname');
  let check_nickname = false;

  userNickname.blur(() => {
    let nicknameVal = userNickname.val();
    let nicknameRegExp = /^[가-힣]{1,5}$/; // 1~5자의 한글만 허용하는 정규식

    if (nicknameVal === "" || !nicknameRegExp.test(nicknameVal)) {
      checkNickname.html("닉네임은 1~5자의 한글만 입력 가능합니다.");
      checkNickname.css('color', 'red');
      check_nickname = false;
      updateSubmitButton(); // 가입 버튼 상태 업데이트 (필요한 경우)
      return;
    }

    // 닉네임 중복 확인 Ajax 요청
    $.ajax({
      type: "POST",
      url: "/nicknameCheck",
      data: { "UserNickname": nicknameVal }, // 파라미터 이름 확인
      dataType: "text",
      success: function (result) {
        if (result == "OK") {
          checkNickname.html(nicknameVal + "는 사용 가능한 닉네임입니다.");
          checkNickname.css('color', 'blue');
          check_nickname = true;
        } else {
          checkNickname.html(nicknameVal + "는 이미 사용중인 닉네임입니다.");
          checkNickname.css('color', 'red');
          check_nickname = false;
        }
        updateSubmitButton();
      },
      error: function () {
        alert('nicknameCheck 통신 실패!');
      }
    });
  });
  // 다음 주소 API
  function sample6_execDaumPostcode() {
    new daum.Postcode({
      oncomplete: function(data) {
        var addr = ''; // 주소 변수
        var extraAddr = ''; // 참고항목 변수

        if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
          addr = data.roadAddress;
        } else { // 사용자가 지번 주소를 선택했을 경우(J)
          addr = data.jibunAddress;
        }

        document.getElementById("sample6_address").value = addr;
        document.getElementById("sample6_detailAddress").focus();
      }
    }).open();
  }
  //유저 프로파일 미리보기
  $('#User_Profile').on('change', function(event) {
    let file = event.target.files[0];  // 선택한 파일
    console.log(file);  // 파일 객체가 잘 읽혔는지 확인
    if (file) {
      let reader = new FileReader();  // FileReader 생성

      reader.onload = function(e) {
        console.log(e.target.result);  // 읽힌 파일의 URL을 콘솔로 출력하여 확인
        $('#preImage').attr('src', e.target.result);  // 미리보기 이미지
      };

      // 파일을 Base64 데이터 URL로 변환
      reader.readAsDataURL(file);  // FileReader가 Base64로 변환
    } else {
      console.log('파일이 선택되지 않았습니다');
    }
  });

  // 주소 합치기
  $('#submitForm').click(()=>{
    let user_Address = $('#User_Address');

    let addr1 = $('#sample6_address').val();
    let addr2 = $('#sample6_detailAddress').val();

    user_Address.val(`${addr1}, ${addr2}`);
    // 인천시 미추홀구 매소홀로 6-32, 태승빌딩 5층

    if(!check_id){
      alert('아이디 중복체크를 진행해주세요');
    } else if(!check_pw1 || !check_pw2){
      alert('비밀번호를 확인해주세요');
    } else if(!check_email){
      alert('이메일 인증을 진행해주세요');

    } else if(!check_phone){
      alert('휴대폰 인증을 진행해주세요')
    }
    else {
      mJoinForm.submit();
    }

  });

  // 휴대전화 관련 함수

  let check_phone = false;

  // 연락처 인증번호 발송
  document.getElementById("checkPhone").addEventListener("click", function() {
    var phone = document.getElementById("User_Phone").value;

    if (phone === "") {
      alert("연락처를 입력해주세요.");
      return;
    }else{

      alert("입력하신 폰 번호는 : " + phone +"입니다.")
    }

    // AJAX로 인증번호 생성 요청 (변경된 URL '/PhoneCheck')
    $.ajax({
      url: '/PhoneCheck',  // URL을 /PhoneCheck로 변경
      method: 'POST',
      data: { "UserPhone": phone },
      dataType: "text",
      success: function(response) {
        // 서버에서 인증번호 생성 후 반환


        console.log("연락처:", phone, "인증번호:", response);

        // 인증번호 입력 필드와 버튼을 check5에 동적으로 추가
        var check5 = $('#check5');
        check5.empty();  // 기존 내용 비우기
        check5.append(`<br/><input type="text" id="uuid" />`);  // 인증번호 입력 필드 추가
        check5.append(` <input type="button" value="인증" id="BtnPhone" data-value="${response}" />`);  // 인증 확인 버튼 추가

        check_phone = true;
      },
      error: function(error) {
        alert('인증번호를 생성하는데 실패했습니다. 다시 시도해주세요.');
      }
    });
  });

  // 인증번호 확인
  $(document).on('click', '#BtnPhone', function(){
    if (!check_phone) {
      alert("먼저 인증번호 발송을 클릭해주세요.");
      return;
    }

    // 입력한 인증번호
    let btnPhone = $('#uuid').val();
    let btnPhone1 = $(this).data("value");

    if (btnPhone === btnPhone1) {
      alert('연락처 인증 성공');
      $("#check5").hide();  // 인증 성공 후 인증 필드 숨김
      $('#User_Phone').attr('readonly', true);  // 연락처를 읽기 전용으로 설정
    } else {
      alert('인증번호를 확인해주세요');
      $('#uuid').val("");  // 입력 필드 초기화
    }
  });

  function selectOption(selectedValue) {
    // 성별 선택에서만 active 클래스 적용
    const buttons = document.querySelectorAll('#genderButtons .option'); // 성별 선택 버튼만 선택
    buttons.forEach(button => button.classList.remove('active')); // 모든 성별 버튼에서 active 클래스 제거

    // 클릭된 버튼에 active 클래스 추가
    const selectedButton = event.target;
    selectedButton.classList.add('active');

    // 선택된 값 저장
    document.getElementById('GType').value = selectedValue; // 성별 값을 hidden input에 저장

    // 선택된 값을 'selected' div에 표시 (옵션)
    document.getElementById('selectedOption').textContent = selectedValue;
  }

  function selectOption2(value) {
    // 자차유무 선택에서만 active 클래스 적용
    const buttons = document.querySelectorAll('#carButtons .option'); // 자차유무 버튼만 선택
    buttons.forEach(button => button.classList.remove('active')); // 모든 자차유무 버튼에서 active 클래스 제거

    // 클릭된 버튼에 active 클래스 추가
    const selectedButton = event.target;
    selectedButton.classList.add('active');

    // 선택된 값 저장
    document.getElementById('CType').value = value; // 자차유무 값을 hidden input에 저장

    // 선택된 값을 'selected' div에 표시 (옵션)
    document.getElementById('selectedOption2').textContent = value === 1 ? 'o' : 'x';
  }
