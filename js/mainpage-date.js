document.addEventListener("DOMContentLoaded", function () {
    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, "0"); // 월은 0부터 시작하므로 +1 필요
    const day = String(today.getDate()).padStart(2, "0");
    
    const formattedDate = `${year}.${month}.${day}`;
    
    document.getElementById("date").innerHTML = formattedDate;
});
