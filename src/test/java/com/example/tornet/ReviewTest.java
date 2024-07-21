package com.example.tornet;

import com.example.tornet.model.Customer;
import com.example.tornet.model.Product;
import com.example.tornet.model.Review;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PrePersist;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class ReviewTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void createReviewTest() {
        Customer customer = new Customer();
        customer.setEmail("tom@gmail.com");

        // Persist the customer first to ensure it exists in the database
        entityManager.persist(customer);
        entityManager.flush();

        Product product = new Product();
        product.setName("Example Product");

        // Persist the product to ensure it exists in the database
        entityManager.persist(product);
        entityManager.flush();

        Review review = new Review();
        review.setCustomer(customer);
        review.setProduct(product);
        review.setCommets("Great product");
        review.setCreateComments(new Date());
        review.setLikes(5);

        entityManager.persist(review);
        entityManager.flush();

        assertNotNull(review.getId());
        assertNotNull(review.getProduct());
        assertNotNull(review.getCustomer());
    }
}
