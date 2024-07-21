document.addEventListener("DOMContentLoaded", function() {
    const toggleButton = document.getElementById("toggleButton");
    const categories = document.getElementById("categories");

    toggleButton.addEventListener("click", function() {
        categories.classList.toggle("show");
    });
});
// Получаем кнопку и меню
var userBtn = document.getElementById("userBtn");
var userDropdown = document.getElementById("userDropdown");

// Открытие/закрытие меню при клике на кнопку
userBtn.onclick = function() {
    userDropdown.classList.toggle("show");
}

// Закрытие меню при клике вне его области
window.onclick = function(event) {
    if (!event.target.matches('.user-btn')) {
        var dropdowns = document.getElementsByClassName("dropdown-content");
        for (var i = 0; i < dropdowns.length; i++) {
            var openDropdown = dropdowns[i];
            if (openDropdown.classList.contains('show')) {
                openDropdown.classList.remove('show');
            }
        }
    }
}
