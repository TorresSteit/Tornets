package com.example.tornet.service;

import com.example.tornet.exception.ProductNotFoundException;
import com.example.tornet.model.Category;
import com.example.tornet.model.Product;
import com.example.tornet.model.ProductInfo;
import com.example.tornet.reposotory.CategoryRepository;
import com.example.tornet.reposotory.ProductInfoRepository;
import com.example.tornet.reposotory.ProductRepostory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final CategoryRepository categoryRepository;
    private final ProductRepostory productRepostory;
    private final ProductInfoRepository productInfoRepository;







    public List<Category> getAllCategoriesWithProducts() {
        List<Category> categories = categoryRepository.findAll();

        for (Category category : categories) {
            List<Product> products = category.getProducts();
            category.setProducts(products);
        }

        return categories;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }



    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElse(null);
    }

    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }


    @Transactional
    public void addProduct(Product product, List<ProductInfo> productInfoList) {
        productRepostory.save(product);
        for (ProductInfo productInfo : productInfoList) {
            productInfo.setProduct(product);
            productInfoRepository.save(productInfo);
        }
    }
    public List<Product> findByPattern(String pattern) {
        return productRepostory.findByNameContainingIgnoreCase(pattern);
    }



    public List<Product> getAllProducts() {
        return productRepostory.findAll();
    }

    @Transactional
    public Product getProductById(Long id) {

        return productRepostory.findByIdWithProductInfo(id);
    }
    public Product findProductById(Long productId) {
        Optional<Product> optionalProduct = productRepostory.findById(productId);
        return optionalProduct.orElse(null);
    }


    public Page<Product> getProductsByPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepostory.findAll(pageable);
    }
    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepostory.findByCategoryId(categoryId);
    }
    public List<Product> findAll() {
        return productRepostory.findAll();
    }


    @Transactional
    public void deleteProductById(Long id) {
        Optional<Product> product = productRepostory.findById(id);
        if (product.isPresent()) {
            log.info("Delete ID: {}", id);
            productRepostory.delete(product.get());
        } else {
            log.warn("Продукт с ID {} не найден", id);
            throw new ResourceNotFoundException("Продукт с ID " + id + " не найден");
        }
    }


    @Transactional
    public void updateProduct(Product product) {
        productRepostory.save(product);
    }


    public Page<Product> getProductsPage(Pageable pageable) {
        return productRepostory.findAll(pageable);
    }

    public List<Product> searchProductsByName(String name) {
        return productRepostory.findByNameContainingIgnoreCase(name);
    }


}
