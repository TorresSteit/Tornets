package com.example.tornet.service;

import com.example.tornet.model.*;
import com.example.tornet.reposotory.CartRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class CartService {
    private  final CartRepository cartRepository;
@Transactional
    public Cart save(Cart cart) {
        cart.setCustomer(new Customer());
        cart.setProduct(new Product());
        cart.setCreateCart(new Date());
        cart.setOrder(new Order());
        cart.setTotalPrice(new BigDecimal(0.0));

        return cartRepository.save(cart);
    }
    private BigDecimal calculateTotalPrice(List<Product> products, List<ProductInfo> productInfos) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            ProductInfo productInfo = productInfos.get(i);
            totalPrice = totalPrice.add(product.getPrice().multiply(BigDecimal.valueOf(productInfo.getQuantity())));
        }
        return totalPrice;
    }


}
