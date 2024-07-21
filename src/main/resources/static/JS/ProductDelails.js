document.addEventListener("DOMContentLoaded", function() {
    document.querySelectorAll('.size-button').forEach(button => {
        button.addEventListener('click', function() {
            let size = this.getAttribute('data-size');
            selectSize(size);
        });
    });
});

function selectSize(productId, size) {
    fetch(`/selectSize?productId=${productId}&selectedSize=${size}`)
        .then(response => response.text())
        .then(html => {
            document.getElementById('sizeSelectionFragment').innerHTML = html;
        });
}




