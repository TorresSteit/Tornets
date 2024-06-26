package com.example.tornet.reposotory;

import com.example.tornet.model.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProductInfoRepository extends JpaRepository<ProductInfo, Long> {
    Optional<ProductInfo> findById(Long id);
    List<ProductInfo> findByProductId(Long productId);
}
