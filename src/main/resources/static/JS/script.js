// Получаем кнопку и список категорий
const toggleBtn = document.getElementById('toggleBtn');
const categoriesList = document.getElementById('categoriesList');

// Добавляем обработчик события для кнопки
toggleBtn.addEventListener('click', function() {
    // Переключаем видимость списка категорий
    categoriesList.classList.toggle('hidden');
});

