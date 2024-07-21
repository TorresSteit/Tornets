package com.example.tornet.service;





import com.example.tornet.model.Cart;
import com.example.tornet.model.Customer;
import com.example.tornet.model.Product;
import com.example.tornet.model.ProductInfo;
import com.example.tornet.reposotory.CartRepository;
import com.example.tornet.reposotory.ProductInfoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class CartService {

    private final CartRepository cartRepository;
    private final CustomerService customerService;
    private final ProductService productService;
    private final ProductInfoRepository productInfoRepository;

    @Transactional
    public boolean addProductToCart(Customer customer, Long productId, String size, int quantity) {
        try {
            // Находим продукт по ID
            Product product = productService.findProductById(productId);
            if (product == null) {
                throw new IllegalArgumentException("Продукт не найден с ID: " + productId);
            }

            // Находим корзину клиента, если она существует, иначе создаем новую
            Cart cart = cartRepository.findByCustomerAndOrderIsNull(customer)
                    .orElseGet(() -> {
                        Cart newCart = new Cart();
                        newCart.setCustomer(customer);
                        newCart.setCreateCart(new Date());
                        return newCart;
                    });

            // Проверяем, существует ли уже продукт с тем же ID и размером в корзине
            Optional<ProductInfo> existingProductInfoOpt = cart.getProductInfos().stream()
                    .filter(info -> info.getProduct().getId().equals(productId) && info.getSizes().contains(size))
                    .findFirst();

            if (existingProductInfoOpt.isPresent()) {
                // Обновляем количество, если продукт уже существует в корзине
                ProductInfo existingProductInfo = existingProductInfoOpt.get();
                existingProductInfo.setQuantity(existingProductInfo.getQuantity() + quantity);
            } else {
                // Создаем новый продукт, если его нет в корзине
                ProductInfo productInfo = new ProductInfo();
                productInfo.setProduct(product);

                List<String> sizes = new ArrayList<>();
                sizes.add(size);
                productInfo.setSizes(sizes);
                productInfo.setQuantity(quantity);

                productInfo.setCart(cart);
                cart.getProductInfos().add(productInfo);
            }

            // Пересчитываем общую стоимость корзины
            BigDecimal totalPrice = calculateTotalPrice(cart.getProductInfos());
            cart.setTotalPrice(totalPrice);

            // Сохраняем изменения в базе данных
            cartRepository.save(cart);

            return true;
        } catch (Exception e) {
            log.error("Ошибка при добавлении продукта в корзину для покупателя {}: {}", customer.getEmail(), e.getMessage(), e);
            throw new RuntimeException("Ошибка при добавлении продукта в корзину", e);
        }
    }





    private BigDecimal calculateTotalPrice(List<ProductInfo> productInfos) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (ProductInfo productInfo : productInfos) {
            Product product = productInfo.getProduct();
            BigDecimal productPrice = BigDecimal.valueOf(product.getPrice());
            int quantity = productInfo.getQuantity();
            totalPrice = totalPrice.add(productPrice.multiply(BigDecimal.valueOf(quantity)));
        }
        return totalPrice;
    }

    public Cart getCart(Long cartId) {
        return cartRepository.findById(cartId).orElse(null);
    }

    public Cart getCartByCustomer(Customer customer) {
        return cartRepository.findByCustomerAndOrderIsNull(customer).orElse(null);
    }

    public Page<ProductInfo> getCartPage(Cart cart, Pageable pageable) {
        return productInfoRepository.findByCart(cart, pageable);
    }

    @Transactional
    public void removeCart(Long cartId) {
        productInfoRepository.deleteByCartId(cartId);
        cartRepository.deleteById(cartId);
    }

    @Transactional
    public void clearCart(Customer customer) {
        log.debug("Clearing cart for customer: {}", customer.getEmail());
        Cart cart = cartRepository.findByCustomerAndOrderIsNull(customer).orElse(null);
        if (cart != null) {
            log.debug("Found cart for customer: {}", customer.getEmail());
            cart.getProductInfos().clear();
            cartRepository.save(cart);
            log.debug("Cart cleared and saved for customer: {}", customer.getEmail());
        } else {
            log.debug("No cart found for customer: {}", customer.getEmail());
        }
    }

    public List<Cart> getCartsByCustomerId(Long customerId) {
        return cartRepository.findByCustomerId(customerId);
    }

    @Transactional
    public void deleteProductFromCart(Long productId, String customerEmail) {
        // Найти покупателя по его электронной почте
        Customer customer = customerService.findCustomerByEmail(customerEmail);
        Cart cart = cartRepository.findByCustomerAndOrderIsNull(customer).orElse(null);
        if (cart != null) {
            // Получить список товаров в корзине
            List<ProductInfo> productInfos = cart.getProductInfos();

            // Найти информацию о продукте в корзине по productId
            Optional<ProductInfo> productInfoOptional = productInfos.stream()
                    .filter(productInfo -> productInfo.getProduct().getId().equals(productId))
                    .findFirst();

            // Удалить информацию о продукте из корзины, если она найдена
            productInfoOptional.ifPresent(productInfo -> {
                productInfos.remove(productInfo);
                cartRepository.save(cart);
            });
        }
    }

    public void saveCart(Cart cart) {
        cartRepository.save(cart);
    }
}

