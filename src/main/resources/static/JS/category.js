document.addEventListener("DOMContentLoaded", function() {
    const toggleButton = document.getElementById("toggleButton");
    const categories = document.getElementById("categories");

    toggleButton.addEventListener("click", function() {
        categories.classList.toggle("show");
    });
});
