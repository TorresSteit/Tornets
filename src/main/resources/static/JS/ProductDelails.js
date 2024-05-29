function selectSize(size) {
    // Update the selected size text
    document.getElementById("selectedSize").innerText = "Selected Size: " + size;
    // Update the value of the hidden input field
    document.getElementById("selectedSizeInput").value = size;
}
document.addEventListener('DOMContentLoaded', function() {
    const starsContainers = document.querySelectorAll('.stars');

    starsContainers.forEach(starsContainer => {
        const stars = starsContainer.children;

        starsContainer.addEventListener('mouseover', function(event) {
            const target = event.target;
            const index = target.getAttribute('data-index');

            if (index) {
                for (let i = 0; i < index; i++) {
                    stars[i].classList.add('hover');
                }
            }
        });

        starsContainer.addEventListener('mouseout', function() {
            starsContainer.querySelectorAll('.fas').forEach(star => {
                star.classList.remove('hover');
            });
        });

        starsContainer.addEventListener('click', function(event) {
            const target = event.target;
            const index = target.getAttribute('data-index');

            if (index) {
                starsContainer.setAttribute('data-rating', index);
            }
        });
    });
});




