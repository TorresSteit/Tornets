document.addEventListener('DOMContentLoaded', function() {
    const categorySection = document.getElementById('categories');
    const productSection = document.getElementById('products');
    const clientSection = document.getElementById('clients');
    const orderSection = document.getElementById('order'); // Добавляем обработку секции заказов

    const categoryButton = document.getElementById('category-button');
    const productButton = document.getElementById('product-button');
    const clientButton = document.getElementById('client-button');
    const orderButton = document.getElementById('order-button'); // Добавляем обработку кнопки заказов

    // Переключение на раздел категорий
    categoryButton.addEventListener('click', function() {
        categorySection.style.display = 'block';
        productSection.style.display = 'none';
        clientSection.style.display = 'none';
        orderSection.style.display = 'none';
    });

    // Переключение на раздел продуктов
    productButton.addEventListener('click', function() {
        categorySection.style.display = 'none';
        productSection.style.display = 'block';
        clientSection.style.display = 'none';
        orderSection.style.display = 'none';
    });

    // Переключение на раздел клиентов
    clientButton.addEventListener('click', function() {
        categorySection.style.display = 'none';
        productSection.style.display = 'none';
        clientSection.style.display = 'block';
        orderSection.style.display = 'none';
    });

    // Переключение на раздел заказов
    orderButton.addEventListener('click', function() {
        categorySection.style.display = 'none';
        productSection.style.display = 'none';
        clientSection.style.display = 'none';
        orderSection.style.display = 'block';
    });


});


