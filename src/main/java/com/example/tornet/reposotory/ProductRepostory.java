package com.example.tornet.reposotory;

import com.example.tornet.model.Category;
import com.example.tornet.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductRepostory extends JpaRepository<Product, Long> {

    Product findByName(String name);

    // Найти продукт по его бренду


    // Найти продукты по категории
    List<Product> findByCategory(Category category);

    // Найти продукты по цене, меньше указанной
    List<Product> findByPriceLessThan(BigDecimal price);

    // Найти продукты по цене, больше указанной
    List<Product> findByPriceGreaterThan(BigDecimal price);

    List<Product> findByCategoryId(Long categoryId);

    Optional<Product> findById(Long id);
    

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.productInfo WHERE p.id = :id")
    Product findByIdWithProductInfo(@Param("id") Long id);
    boolean existsById(Long id);
    void deleteById(Long id);


}
