package com.example.tornet.controller;

import com.example.tornet.exception.CustomerNotFoundException;
import com.example.tornet.model.*;
import com.example.tornet.reposotory.ProductInfoRepository;
import com.example.tornet.service.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;


import java.util.Date;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Controller
@Slf4j
public class MainController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final CustomerService customerService;
    private final ProductInfoRepository productInfoRepository;
    private  final CartService cartService;
    private final ReviewService reviewService;

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


        String encodedImage = Base64Utils.encodeToString(product.getImage());

        // Add attributes to the model
        model.addAttribute("product", product);
        model.addAttribute("productInfoList", productInfoList);
        model.addAttribute("encodedImage", encodedImage);

        // Return the view name
        return "product";
    }

    @PostMapping("/add_to_cart")
    public String addToCart(@ModelAttribute("cart") Cart cart, Model model, @RequestParam("productId") Long productId) {
        // Retrieve product based on productId
        Product product = productService.getProductById(productId);

        if (product != null) {
            // Create a new ProductInfo instance and set the product
            ProductInfo productInfo = new ProductInfo();
            productInfo.setProduct(product);
            productInfo.setQuantity(1); // Set initial quantity to 1, adjust as needed

            // Add productInfo to the cart's productInfos collection
            cart.getProductInfos().add(productInfo);

            // Retrieve current logged-in customer
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            Customer customer = customerService.findCustomerByEmail(currentPrincipalName);

            // Set customer for the cart
            cart.setCustomer(customer);

            // Save the updated cart
            cartService.save(cart);

            // Add attributes to the model
            model.addAttribute("cart", cart);
            model.addAttribute("message", "Product successfully added to the cart"); // Success message
        } else {
            model.addAttribute("message", "Error adding product to the cart"); // Error message
        }

        return "Cart"; // Return the name of the view to render
    }

    @PostMapping("/addReview")
    public String addReview(@ModelAttribute("review") Review review, Model model) {

        review.setCreate_coments(new Date());


        reviewService.addReview(review);


        Long productId = review.getProduct().getId();
        log.info("Added review for product with ID: {}", productId);



        return "redirect:/product/" + productId;
    }
    @GetMapping("/product")
    public String getProductReviews(@RequestParam Long id, Model model) {
        // Fetch reviews for the product with given id
        List<Review> reviews = reviewService.getReviewsByProductId(id);

        // Add reviews to the model
        model.addAttribute("reviews", reviews);
        return "productReviews"; // Assuming "productReviews" is your view name to display reviews
    }

    @PostMapping("/likeProduct/{productId}")
    @ResponseBody
    public ResponseEntity<String> likeProduct(@PathVariable Long productId) {
        // Retrieve the product by ID
        Product product = productService.getProductById(productId);

        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        Review review=new Review();
        review.setProduct(product);
        review.setLikes(review.getLikes()+1  );

        // Return a success message or status code
        return ResponseEntity.ok("Product liked successfully");
    }


}

