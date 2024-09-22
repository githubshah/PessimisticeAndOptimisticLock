package com.example.pessimisticlockingdemo;

import com.example.pessimisticlockingdemo.entity.Product;
import com.example.pessimisticlockingdemo.repository.ProductRepository;
import com.example.pessimisticlockingdemo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class PessimisticLockingTest implements CommandLineRunner {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        // Initialize a product
        Product product = new Product();
        product.setName("Laptop");
        product.setQuantity(10);
        product = productService.saveProduct(product);

        Long productId = product.getId();

        CountDownLatch latch = new CountDownLatch(2);

        // Thread 1: Decrease quantity by 5
        new Thread(() -> {
            try {
                System.out.println("Thread 1: Starting transaction to decrease quantity by 5");
                productService.decreaseQuantity(productId, 5);
                System.out.println("Thread 1: Transaction completed");
            } catch (Exception e) {
                System.err.println("Thread 1: " + e.getMessage());
            } finally {
                latch.countDown();
            }
        }).start();

        // Thread 2: Decrease quantity by 6 (should fail due to insufficient quantity)
        new Thread(() -> {
            try {
                // Sleep to ensure Thread 1 acquires the lock first
                Thread.sleep(100);
                System.out.println("Thread 2: Starting transaction to decrease quantity by 6");
                productService.decreaseQuantity(productId, 6);
                System.out.println("Thread 2: Transaction completed");
            } catch (Exception e) {
                System.err.println("Thread 2: " + e.getMessage());
            } finally {
                latch.countDown();
            }
        }).start();

        latch.await();

        // Final product quantity
        Product finalProduct = productService.getProduct(productId);
        System.out.println("Final product quantity: " + finalProduct.getQuantity());
    }
}
