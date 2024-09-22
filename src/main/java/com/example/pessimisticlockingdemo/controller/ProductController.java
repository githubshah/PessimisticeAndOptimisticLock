package com.example.pessimisticlockingdemo.controller;

import com.example.pessimisticlockingdemo.entity.Product;
import com.example.pessimisticlockingdemo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * Create a new product.
     */
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    /**
     * Get product details.
     */
    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    /**
     * Decrease product quantity.
     */
    @PostMapping("/{id}/decrease")
    public String decreaseQuantity(@PathVariable Long id, @RequestParam int amount) {
        productService.decreaseQuantity(id, amount);
        return "Quantity decreased successfully";
    }

    /**
     * Increase product quantity.
     */
    @PostMapping("/{id}/increase")
    public String increaseQuantity(@PathVariable Long id, @RequestParam int amount) {
        productService.increaseQuantity(id, amount);
        return "Quantity increased successfully";
    }
}