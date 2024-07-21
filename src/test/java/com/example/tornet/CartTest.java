package com.example.tornet;

import com.example.tornet.model.*;
import com.example.tornet.service.CartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class CartTest {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private CartService cartService;

    @Test
    public void createCartWithProductInfoTest() {
        Customer customer = new Customer();
        customer.setEmail("customer@example.com");


        entityManager.persist(customer);
        entityManager.flush();

        Product product = new Product();
        product.setName("Example Product");

        // Persist the product to ensure it exists in the database
        entityManager.persist(product);
        entityManager.flush();

        Cart cart = new Cart();
        cart.setCustomer(customer);

        cart.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        cart.setTotalPrice(BigDecimal.valueOf(100.00));
        cart.setCreateCart(new Date());

         ProductInfo productInfo = new ProductInfo();
        productInfo.setSizes(Collections.singletonList("M, L"));
        productInfo.setDescriptions("Sample product info");
        productInfo.setQuantity(10);
        productInfo.setProduct(product);
        productInfo.setCart(cart);


        cart.getProductInfos().add(productInfo);

        entityManager.persist(cart);
        entityManager.persist(productInfo);
        entityManager.flush();

        assertNotNull(cart.getId());
        assertNotNull(productInfo.getId());
        assertNotNull(productInfo.getCart());
    }

}

