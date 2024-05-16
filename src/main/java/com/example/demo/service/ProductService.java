package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        try {
            return productRepository.findAll();
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while retrieving products", ex);
        }
    }

    public Product getProductById(String id) {
        Optional<Product> product;
        try {
            product = productRepository.findById(id);
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while retrieving product with id: " + id, ex);
        }

        if (product.isPresent()) {
            return product.get();
        } else {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
    }

    public Product saveProduct(Product product) {
        try {
            return productRepository.save(product);
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while saving product", ex);
        }
    }

    public void deleteProduct(String id) {
        try {
            if (productRepository.existsById(id)) {
                productRepository.deleteById(id);
            } else {
                throw new ResourceNotFoundException("Product not found with id: " + id);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while deleting product with id: " + id, ex);
        }
    }

}