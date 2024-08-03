package com.example.tornet.controller;

import com.example.tornet.model.*;
import com.example.tornet.reposotory.CartRepository;
import com.example.tornet.reposotory.ProductInfoRepository;
import com.example.tornet.service.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@AllArgsConstructor
@Controller
@Slf4j
public class MainController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final CustomerService customerService;
    private final ProductInfoRepository productInfoRepository;
    private final ReviewService reviewService;
    private final CartService cartService;
    private final OrderService orderService;
    private  final EmailSenderService emailSenderService;


    @GetMapping("/Main")
    public String mainPage() {



        return "Main";
    }
    @PostMapping(value = "/search")
    public String search(@RequestParam String pattern, Model model) {
        List<Product> products = productService.findByPattern(pattern);
        model.addAttribute("products", products);
        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categories", categoryList);
        return "Main";
    }



    @GetMapping("/Cart")
    public String cartPage(Model model, @RequestParam(name = "page", defaultValue = "0") int page, RedirectAttributes redirectAttributes) {
        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
                log.warn("No authenticated user found or user details not found in security context");
                redirectAttributes.addFlashAttribute("error", "Authentication error: No authenticated user found");
                return "redirect:/error-page";
            }

            String currentPrincipalName = authentication.getName();


            Customer customer = customerService.findCustomerByEmail(currentPrincipalName);
            log.info("Email customer", currentPrincipalName);
            if (customer == null) {
                log.warn("Customer not found for email: {}", currentPrincipalName);
                redirectAttributes.addFlashAttribute("error", "Customer not found for email: " + currentPrincipalName);
                return "redirect:/error-page";
            }


            Pageable pageable = PageRequest.of(page, 6);


            Cart cart = cartService.getCartByCustomer(customer);
            if (cart == null) {
                cart = new Cart();
                cart.setCustomer(customer);
                cart.setProductInfos(new ArrayList<>());
                cartService.saveCart(cart);
                log.info("Initialized a new cart for customer: {}", currentPrincipalName);
            }


            Page<ProductInfo> productInfoPage = cartService.getCartPage(cart, pageable);


            model.addAttribute("cart", cart);
            model.addAttribute("productInfoPage", productInfoPage);
            model.addAttribute("totalPages", productInfoPage.getTotalPages());
            model.addAttribute("currentPage", page);

            return "Cart";
        } catch (Exception e) {

            log.error("An error occurred while fetching the cart page: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "An error occurred while fetching the cart page");
            return "redirect:/error-page";
        }
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
        List<ProductInfo> productInfoList = productInfoRepository.findAllByProduct_Id(id);
        String encodedImage = Base64Utils.encodeToString(product.getImage());
        List<Review> reviews = reviewService.getReviewsByProductId(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = null;
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            userEmail = userDetails.getUsername();
        }
        Customer customer = new Customer();
        if (userEmail != null) {
            customer = customerService.findCustomerByEmail(userEmail);
        }
        //цей мемент трохи не роузмів як перейти по айдшінку на сторінку інформаціїб це знайшов на стек офревловб 404 помилка


        model.addAttribute("product", product);
        model.addAttribute("productInfoList", productInfoList);
        model.addAttribute("encodedImage", encodedImage);
        model.addAttribute("reviews", reviews);
        model.addAttribute("customer", customer);

        return "product";
    }


    @PostMapping("/addReview")
    public String addReview(@ModelAttribute("review") Review review, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        try {
            Customer customer = customerService.findCustomerByEmail(currentPrincipalName);
            if (customer == null) {
                throw new IllegalArgumentException("Customer not found with email: " + currentPrincipalName);
            }

            Long productId = review.getProduct().getId();


            Product product = productService.getProductById(productId);
            if (product == null) {
                model.addAttribute("error", "Invalid product ID: " + productId);
                return "errorPage";
            }

            review.setCustomer(customer);
            review.setProduct(product);
            review.setCreateComments(new Date());
            reviewService.addReview(review);

            log.info("Added review for product with ID: {}", productId);
            //чогось не показує комнетиб але не знаю в чом причина

            return "redirect:/product/" + productId;
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred: " + e.getMessage());
            return "ErrorPage";
        }
    }



    @GetMapping("/product")
    public String getProductReviews(@RequestParam Long id, Model model) {
        try {
            List<Review> reviews = reviewService.getReviewsByProductId(id);
            for (Review review : reviews) {
                if (review.getCommets() == null || review.getCommets().isEmpty()) {
                    review.setCommets("No Comments");
                }
            }
            model.addAttribute("reviews", reviews);
            return "product"; // Ensure this is the correct template name
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred: " + e.getMessage());
            return "ErrorPage";
        }
    }

    @PostMapping("/likeProduct/{productId}")
    @ResponseBody
    public ResponseEntity<String> likeProduct(@PathVariable Long productId) {
        try {
            Product product = productService.getProductById(productId);
            if (product == null) {
                return ResponseEntity.notFound().build();
            }

            Review review = reviewService.getReviewByProductId(productId);
            if (review == null) {
                return ResponseEntity.notFound().build();
            }

            review.setLikes(review.getLikes() + 1);
            reviewService.updateReview(review);

            return ResponseEntity.ok("Product liked successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }







    @PostMapping("/cart/add")
    public String addToCart(@RequestParam("productId") Long productId,
                            @RequestParam(name = "quantity", defaultValue = "1") int quantity,
                            @RequestParam("selectedSize")  String selectedSize,
                            Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Customer customer = customerService.findCustomerByEmail(currentPrincipalName);

        if (customer == null) {
            throw new IllegalArgumentException("Customer not found with email: " + currentPrincipalName);
        }

        boolean added = cartService.addProductToCart(customer, productId, selectedSize, quantity);

        if (added) {
            return "redirect:/Cart";

        } else {
            model.addAttribute("error", "Failed to add product to cart.");
            return "redirect:/error-page";
        }
    }




    @DeleteMapping("/Cart/delete/{productId}")
    public String deleteProductFromCart(@PathVariable("productId") Long productId,
                                        @RequestParam("email") String customerEmail,
                                        Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            Customer customer = customerService.findCustomerByEmail(currentPrincipalName);

            if (customer == null) {
                throw new IllegalArgumentException("Customer not found with email: " + currentPrincipalName);
            }

            cartService.deleteProductFromCart(productId, customerEmail);
            return "redirect:/Cart";

        } catch (Exception e) {
            model.addAttribute("error", "Failed to delete product from cart: " + e.getMessage());
            return "error-page";
        }
    }








    @PostMapping("/Order/create")
    public String createOrder(@RequestParam("email") String email,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            log.debug("Current user email: {}", currentPrincipalName);

            Customer customer = customerService.findCustomerByEmail(currentPrincipalName);
            if (customer == null) {
                throw new IllegalArgumentException("Customer not found for email: " + currentPrincipalName);
            }

            List<Cart> carts = cartService.getCartsByCustomerId(customer.getId());
            if (carts.isEmpty()) {
                throw new IllegalArgumentException("Customer's cart is empty.");
            }

            Order order = new Order();
            order.setCreateOrder(new Date());
            order.setCustomer(customer);
            order.setDeliveryStatus(DeliveryStatus.PENDING);
            order.setPaymentMethod(PaymentMethod.CASH);

            for (Cart cart : carts) {
                cart.setOrder(order);
                for (ProductInfo productInfo : cart.getProductInfos()) {
                    productInfo.setOrder(order);
                }
                for (Product product : cart.getProducts()) {
                    product.setOrder(order);
                }
            }
            order.setCarts(carts);

            orderService.saveOrder(order);
            log.info("Order created successfully with ID: {}", order.getId());

            List<Product> products = carts.stream()
                    .flatMap(cart -> cart.getProductInfos().stream())
                    .map(ProductInfo::getProduct)
                    .collect(Collectors.toList());//трохи тут не розумів для чого це просто

            model.addAttribute("products", products);
            model.addAttribute("customer", customer);
            redirectAttributes.addFlashAttribute("successMessage", "Order created successfully!");

            emailSenderService.sendOrderConfirmationEmail(
                    customer.getEmail(),
                    order.getId().toString(),
                    new SimpleDateFormat("dd/MM/yyyy").format(order.getCreateOrder()),
                    products,
                    order
            );

            cartService.clearCart(customer);

            return "redirect:/Order/" + order.getId();
        } catch (Exception e) {
            log.error("Error creating order: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Error creating order: " + e.getMessage());
            return "error-page";
        }
    }





    @GetMapping("/Order/{orderId}")
    public String getOrderDetails(@PathVariable("orderId") Long orderId, Model model) {
        try {
            Order order = orderService.findById(orderId);
            if (order == null) {
                throw new IllegalArgumentException("Order not found for id: " + orderId);
            }

            log.debug("Order: {}", order);
            log.debug("Carts: {}", order.getCarts());
            for (Cart cart : order.getCarts()) {
                log.debug("Cart: {}", cart);
                log.debug("ProductInfos: {}", cart.getProductInfos());
                for (ProductInfo productInfo : cart.getProductInfos()) {
                    log.debug("ProductInfo: {}", productInfo);
                    log.debug("Product: {}", productInfo.getProduct());
                }
            }


            List<ProductInfo> productInfos = order.getCarts().stream()
                    .flatMap(cart -> cart.getProductInfos().stream())
                    .collect(Collectors.toList());

            log.debug("ProductInfos: {}", productInfos);

            model.addAttribute("order", order);
            model.addAttribute("customer", order.getCustomer());
            model.addAttribute("productInfos", productInfos);
            return "Order"; // Убедитесь, что это соответствует вашему имени шаблона Thymeleaf
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error fetching order details: " + e.getMessage());
            return "error-page";
        }
    }










    @GetMapping("/{id}/confirmation")
    public String getOrderConfirmation(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderById(id);
        if (order == null) {
            return "error-page"; // Handle case where order is not found
        }
        model.addAttribute("order", order);
        return "order-confirmation";
    }

    @GetMapping("/Orders-All")
    public String getAllOrders(Model model) {
        List<Order> orders = orderService.findAllOrders();

        for (Order order : orders) {
            for (Cart cart : order.getCarts()) {
                for (ProductInfo productInfo : cart.getProductInfos()) {
                    log.info("Product Image Path: /images/{}", productInfo.getProduct().getImage());
                }
            }
        }

        model.addAttribute("orders", orders);
        return "Orders-All";
    }



}






