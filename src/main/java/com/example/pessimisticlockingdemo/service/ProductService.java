package com.example.pessimisticlockingdemo.service;

import com.example.pessimisticlockingdemo.entity.Product;
import com.example.pessimisticlockingdemo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * Decrease the quantity of a product with pessimistic locking.
     *
     * @param productId the ID of the product
     * @param amount    the amount to decrease
     * @throws IllegalArgumentException if the product is not found or insufficient quantity
     */
    @Transactional
    public void decreaseQuantity(Long productId, int amount) {
        // Acquire a pessimistic write lock on the product
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        if (product.getQuantity() < amount) {
            throw new IllegalArgumentException("Insufficient quantity");
        }

        product.setQuantity(product.getQuantity() - amount);
        productRepository.save(product); // Not strictly necessary; JPA will flush changes
    }

    /**
     * Increase the quantity of a product with pessimistic locking.
     *
     * @param productId the ID of the product
     * @param amount    the amount to increase
     * @throws IllegalArgumentException if the product is not found
     */
    @Transactional
    public void increaseQuantity(Long productId, int amount) {
        // Acquire a pessimistic write lock on the product
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        product.setQuantity(product.getQuantity() + amount);
        productRepository.save(product); // Not strictly necessary; JPA will flush changes
    }

    /**
     * Get product details.
     *
     * @param productId the ID of the product
     * @return the product
     */
    @Transactional(readOnly = true)
    public Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
    }

    @Transactional
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
}

