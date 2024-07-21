package com.example.tornet;

import com.example.tornet.model.Product;
import com.example.tornet.model.ProductInfo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class ProductInfoTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void createTestProductInfo() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setSizes(Collections.singletonList("M, S, V"));
        productInfo.setAdditionalPhoto1(new byte[]{1, 2, 3});
        productInfo.setAdditionalPhoto2(new byte[]{4, 5, 6});
        productInfo.setDescriptions("Дуже крутий продукт");
        productInfo.setQuantity(12);

        entityManager.persist(productInfo);
        entityManager.flush();

        assertNotNull(productInfo.getId());
    }

    @Test
    public void createTestProductInfoWithProduct() {
        Product product = new Product();
        product.setName("Coat");

        ProductInfo productInfo = new ProductInfo();
        productInfo.setSizes(Collections.singletonList("M, S, V"));
        productInfo.setAdditionalPhoto1(new byte[]{1, 2, 3});
        productInfo.setAdditionalPhoto2(new byte[]{4, 5, 6});
        productInfo.setDescriptions("Дуже крутий продукт");
        productInfo.setQuantity(12);
        productInfo.setProduct(product);

        entityManager.persist(product);
        entityManager.persist(productInfo);
        entityManager.flush();

        assertNotNull(productInfo.getId());
        assertNotNull(product.getId());
    }
}

