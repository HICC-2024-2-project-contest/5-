document.addEventListener("DOMContentLoaded", async () => {
    try {
        // 서버에서 데이터 가져오기 (가상의 API 엔드포인트 사용)
        const response = await fetch("https://example.com/api/user"); // 여기에 실제 API 주소 입력
        if (!response.ok) {
            throw new Error("네트워크 응답이 올바르지 않습니다.");
        }
        
        const data = await response.json(); // JSON 데이터 파싱

        // 가져온 데이터를 HTML 요소에 삽입
        document.getElementById("name").innerHTML = data.name || "정보 없음";
        document.getElementById("age").innerHTML = data.age || "정보 없음";
        document.getElementById("email").innerHTML = data.email || "정보 없음";
        
    } catch (error) {
        console.error("데이터를 불러오는 중 오류 발생:", error);
    }
});
