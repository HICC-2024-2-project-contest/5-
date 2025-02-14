document.addEventListener("DOMContentLoaded", () => {
    const ratingScore = document.querySelector(".rating-score");
    const reviewCount = document.querySelector(".review-count");
  
    let totalRating = 4.5; // 초기 별점
    let totalReviews = 128; // 초기 리뷰 수
  
    function updateReviewData(newRating, newReviewCount) {
      // 유효한 숫자인지 체크
      if (isNaN(newRating) || isNaN(newReviewCount)) {
        console.error("잘못된 입력값입니다!");
        return;
      }
  
      totalRating = newRating;
      totalReviews = newReviewCount;
  
      ratingScore.textContent = totalRating.toFixed(1);
      reviewCount.textContent = totalReviews;
    }
  
    // 예제: 3초 후 데이터 변경 (테스트용)
    setTimeout(() => {
      updateReviewData(4.6, 130);
    }, 3000);
  });
  