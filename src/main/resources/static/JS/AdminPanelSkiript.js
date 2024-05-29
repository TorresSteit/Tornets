document.addEventListener('DOMContentLoaded', function() {
    const categorySection = document.getElementById('categories');
    const productSection = document.getElementById('products');
    const clientSection = document.getElementById('Customer'); // Исправлен идентификатор раздела клиентов
    const infoProductSection = document.getElementById('info-product'); // Добавлен идентификатор раздела информации о продукте

    const categoryButton = document.getElementById('category-button');
    const productButton = document.getElementById('product-button');
    const clientButton = document.getElementById('client-button');
    const infoProductButton = document.getElementById('infoproduct-button'); // Исправлен идентификатор кнопки информации о продукте

    // Переключение на раздел категорий
    categoryButton.addEventListener('click', function() {
        categorySection.style.display = 'block';
        productSection.style.display = 'none';
        clientSection.style.display = 'none';
        infoProductSection.style.display = 'none'; // Добавлено скрытие раздела информации о продукте
    });

    // Переключение на раздел продуктов
    productButton.addEventListener('click', function() {
        categorySection.style.display = 'none';
        productSection.style.display = 'block';
        clientSection.style.display = 'none';
        infoProductSection.style.display = 'none'; // Добавлено скрытие раздела информации о продукте
    });

    // Переключение на раздел клиентов
    clientButton.addEventListener('click', function() {
        categorySection.style.display = 'none';
        productSection.style.display = 'none';
        clientSection.style.display = 'block';
        infoProductSection.style.display = 'none'; // Добавлено скрытие раздела информации о продукте
    });

    // Переключение на раздел информации о продукте
    infoProductButton.addEventListener('click', function() {
        categorySection.style.display = 'none';
        productSection.style.display = 'none';
        clientSection.style.display = 'none';
        infoProductSection.style.display = 'block'; // Добавлено отображение раздела информации о продукте
    });

    // По умолчанию показываем раздел категорий
    categoryButton.click();
});
