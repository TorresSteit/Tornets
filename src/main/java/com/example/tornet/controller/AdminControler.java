package com.example.tornet.controller;

import com.example.tornet.model.Category;
import com.example.tornet.model.Product;
import com.example.tornet.model.ProductInfo;
import com.example.tornet.reposotory.ProductInfoRepository;
import com.example.tornet.reposotory.ProductRepostory;
import com.example.tornet.service.CategoryService;
import com.example.tornet.service.CustomerService;
import com.example.tornet.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@AllArgsConstructor
@Slf4j
public class AdminControler {


    private final CustomerService customerService;
    private  final ProductService productService;
    private final CategoryService categoryService;
private final ProductInfoRepository productInfoRepository;
    private final ProductRepostory productRepostory;

    @GetMapping("/AdminPanel")
    public String adminPanel(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("customers", customerService.getAllCustomers());
        return "AdminPanel";
    }

    @PostMapping("/addCategory")
    public ResponseEntity<Category> addCategory(@RequestParam("title") String title) {
        Category category = new Category();
        category.setTitle(title);
        Category savedCategory = categoryService.addCategory(category);
        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }


    // Получение всех категорий
    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }



    @PostMapping("/addProduct")
    public String addProduct(@RequestParam("name") String name,
                             @RequestParam("brand") String brand,
                             @RequestParam("price") BigDecimal price,
                             @RequestParam("description") String description,
                             @RequestParam("mainImage") MultipartFile mainImage,
                             @RequestParam("additionalImages") MultipartFile[] additionalImages,
                             @RequestParam("sizes") String[] sizes,
                             @RequestParam("colors") String[] colors,
                             @RequestParam("categoryId") Long categoryId,
                             Model model) {
        try {
            Category category = categoryService.getCategoryById(categoryId);

            Product product = new Product();
            product.setName(name);
            product.setBrand(brand);
            product.setPrice(price);
            product.setCategory(category);

            if (!mainImage.isEmpty()) {
                byte[] mainImageBytes = mainImage.getBytes();
                product.setImage(mainImageBytes);
            }

            List<ProductInfo> productInfoList = new ArrayList<>();

            for (int i = 0; i < additionalImages.length; i++) {
                MultipartFile additionalImage = additionalImages[i];
                if (!additionalImage.isEmpty()) {
                    byte[] additionalImageBytes = additionalImage.getBytes();

                    ProductInfo productInfo = new ProductInfo();
                    productInfo.setSizes(sizes[i]);
                    productInfo.setColors(colors[i]);
                    productInfo.setDescriptions(description);
                    productInfo.setAdditionalPhotos(additionalImageBytes);

                    productInfoList.add(productInfo);
                }
            }

            // Save the product and its information to the database
            productService.addProduct(product, productInfoList);

        } catch (IOException e) {
            log.error("An error occurred while adding the product: {}", e.getMessage());
            return "redirect:/error";
        }

        return "redirect:/AdminPanel";
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




}














