
    document.addEventListener('DOMContentLoaded', function() {
    const categorySection = document.getElementById('categories');
    const productSection = document.getElementById('products');
    const clientSection = document.getElementById('clients');
    const orderSection = document.getElementById('orders-section'); // Correct ID

    const categoryButton = document.getElementById('category-button');
    const productButton = document.getElementById('product-button');
    const clientButton = document.getElementById('client-button');
    const orderButton = document.getElementById('order-button'); // Correct ID

    categoryButton.addEventListener('click', function() {
    categorySection.style.display = 'block';
    productSection.style.display = 'none';
    clientSection.style.display = 'none';
    orderSection.style.display = 'none';
});

    productButton.addEventListener('click', function() {
    categorySection.style.display = 'none';
    productSection.style.display = 'block';
    clientSection.style.display = 'none';
    orderSection.style.display = 'none';
});

    clientButton.addEventListener('click', function() {
    categorySection.style.display = 'none';
    productSection.style.display = 'none';
    clientSection.style.display = 'block';
    orderSection.style.display = 'none';
});

    orderButton.addEventListener('click', function() {
    categorySection.style.display = 'none';
    productSection.style.display = 'none';
    clientSection.style.display = 'none';
    orderSection.style.display = 'block';
});

    // Show the first section by default
    categorySection.style.display = 'block';
    productSection.style.display = 'none';
    clientSection.style.display = 'none';
    orderSection.style.display = 'none';
});



