package com.example.tornet;

import com.example.tornet.model.Category;
import com.example.tornet.model.Product;
import com.example.tornet.model.ProductInfo;
import com.example.tornet.model.Review;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;



@SpringBootTest
@Transactional
public class ProductTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void testCreateProduct() {
        // Arrange
        Product product = new Product();
        product.setName("Sample Product");
        product.setPrice(19.99);
        product.setImage(new byte[] {1, 2, 3});

        // Act
        entityManager.persist(product);
        entityManager.flush();

        // Assert
        assertNotNull(product.getId());
    }

    @Test
    public void testProductCategoryRelationship() {
        // Arrange
        Category category = new Category();
        category.setTitle("Sample Category");

        Product product = new Product();
        product.setName("Sample Product");
        product.setPrice(19.99);
        product.setCategory(category);

        // Act
        entityManager.persist(category);
        entityManager.persist(product);
        entityManager.flush();

        // Assert
        assertNotNull(product.getId());
        assertNotNull(category.getId());
        assertThat(product.getCategory()).isEqualTo(category);
    }

    @Test
    public void testProductReviewsRelationship() {
        // Arrange
        Product product = new Product();
        product.setName("Sample Product");
        product.setPrice(19.99);

        Review review1 = new Review();
        review1.setProduct(product);
        review1.setCommets("Great product!");

        Review review2 = new Review();
        review2.setProduct(product);
        review2.setCommets("Not bad!");

        product.setReviews(List.of(review1, review2));

        // Act
        entityManager.persist(product);
        entityManager.persist(review1);
        entityManager.persist(review2);
        entityManager.flush();

        // Assert
        Product foundProduct = entityManager.find(Product.class, product.getId());
        assertNotNull(foundProduct);
        assertThat(foundProduct.getReviews()).hasSize(2);
    }
    @Test
    public void testDeleteProduct() {

        Product product = new Product();
        product.setName("Sample Product");
        product.setPrice(19.99);
        product.setImage(new byte[]{1, 2, 3});

        ProductInfo productInfo = new ProductInfo();
        productInfo.setDescriptions("Sample description");
        productInfo.setSizes(List.of("S", "M"));
        productInfo.setQuantity(10);
        productInfo.setProduct(product);
        product.setProductInfo(List.of(productInfo));

        entityManager.persist(product);
        entityManager.flush();


        Product foundProduct = entityManager.find(Product.class, product.getId());
        entityManager.remove(foundProduct);
        entityManager.flush();


        Product deletedProduct = entityManager.find(Product.class, product.getId());
        assertNull(deletedProduct);
    }
}

