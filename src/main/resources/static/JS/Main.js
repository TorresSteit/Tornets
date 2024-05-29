document.addEventListener('DOMContentLoaded', function () {
    // Находим кнопку для открытия и закрытия меню категорий
    var toggleButton = document.querySelector('.toggle-button');

    // Находим меню категорий
    var categoryMenu = document.querySelector('.category-menu');

    // Добавляем обработчик события на клик по кнопке
    toggleButton.addEventListener('click', function () {
        // Переключаем класс, чтобы показать или скрыть меню категорий
        categoryMenu.classList.toggle('opened');
    });
});

