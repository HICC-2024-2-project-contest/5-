document.getElementById('submit-comment').addEventListener('click', function() {
    const commentInput = document.getElementById('comment-input');
    const commentText = commentInput.value.trim();

    if (commentText !== '') {
        const commentDiv = document.createElement('div');
        commentDiv.classList.add('comment');

        // 댓글 내용
        const commentTextElement = document.createElement('p');
        commentTextElement.textContent = commentText;

        // 추천(좋아요) 버튼
        const likeButton = document.createElement('button');
        likeButton.classList.add('like-btn');
        likeButton.textContent = '👍 0';
        likeButton.addEventListener('click', function () {
            let likeCount = parseInt(likeButton.textContent.split(' ')[1]);
            likeCount++;
            likeButton.textContent = `👍 ${likeCount}`;
        });

        commentDiv.appendChild(commentTextElement);
        commentDiv.appendChild(likeButton);

        document.getElementById('comments-list').appendChild(commentDiv);

        commentInput.value = '';
    } else {
        alert('댓글을 입력해주세요.');
    }
});
