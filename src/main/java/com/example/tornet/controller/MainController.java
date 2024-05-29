package com.example.tornet.controller;

import com.example.tornet.model.Category;
import com.example.tornet.model.Product;
import com.example.tornet.model.ProductInfo;
import com.example.tornet.reposotory.ProductInfoRepository;
import com.example.tornet.service.CategoryService;
import com.example.tornet.service.CustomerService;
import com.example.tornet.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Controller

public class MainController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final CustomerService customerService;
    private final ProductInfoRepository productInfoRepository;

    @GetMapping("/Main")
    public String mainPage(Model model) {
        // some logic to fetch data
        List<Product> productList = productService.getAllProducts();
        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("products", productList);
        model.addAttribute("categories", categoryList);
        return "Main"; // Возвращаем имя HTML-шаблона главной страницы
    }



    @RequestMapping("/logout")
    public String logoutPage() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        return "redirect:/login";
    }

    @GetMapping("/Cart")
    public String cartPage(Model model) {
        model.addAttribute("Cart", "/Cart");
        return "Cart";
    }

    @GetMapping("/Main/category/{categoryId}")
    public String getCategory(@PathVariable Long categoryId, Model model) {
        Category category = categoryService.getCategoryById(categoryId);
        if (category == null) {
            return "errorPage";
        }
        model.addAttribute("category", category);

        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        List<Product> productList = productService.getProductsByCategory(categoryId);
        model.addAttribute("products", productList);

        return "Main";


    }
    @GetMapping("/product/{id}")
    public String getProduct(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        List<ProductInfo> productInfoList = productInfoRepository.findByProductId(id);
        model.addAttribute("product", product);
        model.addAttribute("productInfoList", productInfoList);
        return "product";
    }







}

