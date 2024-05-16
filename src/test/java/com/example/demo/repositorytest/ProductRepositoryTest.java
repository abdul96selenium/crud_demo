package com.example.demo.repositorytest;
import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testSaveProduct() {
        Product product = new Product("1", "Test Product", 10.0);
        productRepository.save(product);

        Optional<Product> savedProduct = productRepository.findById("1");
        assertTrue(savedProduct.isPresent());
        assertEquals(product, savedProduct.get());
    }

    @Test
    public void testFindById() {
        Product product = new Product("1", "Test Product", 10.0);
        productRepository.save(product);

        Optional<Product> foundProduct = productRepository.findById("1");
        assertTrue(foundProduct.isPresent());
        assertEquals(product, foundProduct.get());
    }


}
