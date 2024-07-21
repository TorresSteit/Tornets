package com.example.tornet;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.example.tornet.model.Cart;
import com.example.tornet.model.Customer;
import com.example.tornet.model.Product;
import com.example.tornet.model.ProductInfo;
import com.example.tornet.reposotory.CartRepository;
import com.example.tornet.reposotory.ProductInfoRepository;
import com.example.tornet.service.CartService;
import com.example.tornet.service.CustomerService;
import com.example.tornet.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;

public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductInfoRepository productInfoRepository;

    @Mock
    private CustomerService customerService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private CartService cartService;

    private Customer customer;
    private Cart cart;
    private List<ProductInfo> productInfos;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create a customer
        customer = new Customer();
        customer.setId(1L);

        // Create a cart
        cart = new Cart();
        cart.setId(1L);
        cart.setCustomer(customer);
        cart.setCreateCart(new Date());

        // Create productInfos
        productInfos = new ArrayList<>();
        ProductInfo productInfo1 = new ProductInfo();
        productInfo1.setId(1L);
        ProductInfo productInfo2 = new ProductInfo();
        productInfo2.setId(2L);
        productInfos.add(productInfo1);
        productInfos.add(productInfo2);

        cart.setProductInfos(productInfos);
    }
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


}
