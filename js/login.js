document.getElementById("logbtn").addEventListener("click", () => {
    // 입력한 아이디와 비밀번호 가져오기
    const userId = document.getElementById("userid").value;
    const userPw = document.getElementById("userpw").value;

    // 예제: 서버에서 받아온 로그인 정보 (실제 사용 시 fetch로 검증)
    const correctId = "testuser";  // 실제 서버 데이터와 비교하도록 변경해야 함
    const correctPw = "1234";      // 실제 서버 데이터와 비교하도록 변경해야 함

    // 아이디 & 비밀번호 확인
    if (userId === correctId && userPw === correctPw) {
        alert("로그인 성공!");
        window.location.href = "../js/mypage.js"; // 마이페이지로 이동
    } else {
        alert("아이디 또는 비밀번호가 잘못되었습니다.");
    }
});
