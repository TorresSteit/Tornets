package com.example.tornet.controller;

import com.example.tornet.model.Category;
import com.example.tornet.model.Product;
import com.example.tornet.model.ProductInfo;
import com.example.tornet.reposotory.ProductInfoRepository;
import com.example.tornet.reposotory.ProductRepostory;
import com.example.tornet.service.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;


@AllArgsConstructor
@Controller
@Slf4j

public class Admin {


    private final CustomerService customerService;
    private  final ProductService productService;
    private final CategoryService categoryService;
private final ProductInfoRepository productInfoRepository;
    private final ProductRepostory productRepostory;
    private final ProdcutInfoService prodcutInfoService;
    private final OrderService orderService;

    @GetMapping("/Admin")

    public String adminPanel(Model model) {

        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("orders", orderService.getAllOrders());
        return "Admin";
    }

    @PostMapping("/addCategory")
    public ResponseEntity<Category> addCategory(@RequestParam("title") String title) {
        Category category = new Category();
        category.setTitle(title);
        Category savedCategory = categoryService.addCategory(category);
        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }



    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PostMapping("/addProduct")
    public String addProduct(@RequestParam("name") String name,
                             @RequestParam("price") double price,
                             @RequestParam("description") String description,
                             @RequestParam("mainImage") MultipartFile mainImage,
                             @RequestParam("additionalImages1") MultipartFile additionalImages1,
                             @RequestParam("additionalImages2") MultipartFile additionalImages2,
                             @RequestParam("sizes") List<String> sizes,
                             @RequestParam("quantity") int quantity,
                             @RequestParam("categoryId") Long categoryId,
                             Model model) {
        if (quantity < 1) {
            model.addAttribute("error", "Quantity must be at least 1.");
            return "redirect:/error";
        }
        try {
            Category category = categoryService.getCategoryById(categoryId);

            Product product = new Product();
            product.setName(name);
            product.setPrice(price);
            product.setCategory(category);

            if (!mainImage.isEmpty()) {
                byte[] mainImageBytes = mainImage.getBytes();
                product.setImage(mainImageBytes);
            }

            List<ProductInfo> productInfoList = new ArrayList<>();
            for (String size : sizes) {
                ProductInfo productInfo = new ProductInfo();
                productInfo.setDescriptions(description);
                productInfo.setSizes(Collections.singletonList(size)); // Сохраняем каждый размер
                productInfo.setQuantity(quantity);

                if (!additionalImages1.isEmpty()) {
                    byte[] additionImage = additionalImages1.getBytes();
                    productInfo.setAdditionalPhoto1(additionImage);
                }
                if (!additionalImages2.isEmpty()) {
                    byte[] additionImage = additionalImages2.getBytes();
                    productInfo.setAdditionalPhoto2(additionImage);
                }
                productInfoList.add(productInfo);
            }

            productService.addProduct(product, productInfoList);

        } catch (IOException e) {
            log.error("An error occurred while adding the product: {}", e.getMessage());
            return "redirect:/error";
        }

        return "redirect:/Admin";
    }



    @GetMapping("/images/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getProductImage(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product == null || product.getImage() == null) {
            return ResponseEntity.notFound().build();
        }
        byte[] image = product.getImage();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(image, headers, HttpStatus.OK);
    }
    @GetMapping("/additionalPhoto1/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getAdditionalPhoto1(@PathVariable Long id) {
        Optional<ProductInfo> productInfo = prodcutInfoService.getProductInfo(id);
        if (!productInfo.isPresent() || productInfo.get().getAdditionalPhoto1() == null) {
            return ResponseEntity.notFound().build();
        }
        byte[] additionalPhoto1 = productInfo.get().getAdditionalPhoto1();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(additionalPhoto1, headers, HttpStatus.OK);
    }

    @GetMapping("/additionalPhoto2/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getAdditionalPhoto2(@PathVariable Long id) {
        Optional<ProductInfo> productInfo = prodcutInfoService.getProductInfo(id);
        if (!productInfo.isPresent() || productInfo.get().getAdditionalPhoto2() == null) {
            return ResponseEntity.notFound().build();
        }
        byte[] additionalPhoto2 = productInfo.get().getAdditionalPhoto2();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(additionalPhoto2, headers, HttpStatus.OK);
    }


    @DeleteMapping("/Admin/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        try {
            log.info("Попытка удалить продукт с ID: {}", id);
            productService.deleteProductById(id);
        } catch (ResourceNotFoundException e) {
            log.error("Продукт с ID {} не найден", id);
            return "redirect:/error";
        }
        return "redirect:/Admin";
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFoundException(ResourceNotFoundException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "error"; // Убедитесь, что у вас есть шаблон error.html для отображения ошибок
    }








}














