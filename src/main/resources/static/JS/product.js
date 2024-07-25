function selectSize(size) {
    console.log('Selected size: ' + size);  // Добавлено для отладки
    document.getElementById("selectedSizeInput").value = size;
    document.getElementById("selectedSizeDisplay").innerText = size;

    const buttons = document.querySelectorAll('.size-button');
    buttons.forEach(button => button.classList.remove('selected'));
    document.querySelector(`.size-button[data-size="${size}"]`).classList.add('selected');

    const messageDiv = document.getElementById("sizeMessage");
    messageDiv.innerText = 'Selected size: ' + size;
}

function likeReview(reviewId) {
    fetch('/likeReview/' + reviewId, {
        method: 'POST',
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                location.reload();
            } else {
                alert(data.message);
            }
        })
        .catch(error => console.error('Error:', error));
}



