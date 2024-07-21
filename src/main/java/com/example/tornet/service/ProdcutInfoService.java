package com.example.tornet.service;

import com.example.tornet.model.ProductInfo;
import com.example.tornet.reposotory.ProductInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProdcutInfoService {
    private final ProductInfoRepository productInfoRepository;

    public Optional<ProductInfo> getProductInfo(Long Id) {
        return productInfoRepository.findById(Id);
    }
    public List<ProductInfo> getAllProductInfos() {
        return productInfoRepository.findAll();
    }

    public List<ProductInfo> getProductInfosByProductId(Long productId) {
        return productInfoRepository.findAllByProductId(productId);
    }
    public List<ProductInfo> findById(Long Id) {
        return  productInfoRepository.findAllByProductId(Id);
    }
}
