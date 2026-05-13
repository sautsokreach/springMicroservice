package com.microservice.product.service;

import com.microservice.product.model.Product;
import com.microservice.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Optional<Product> getProductBySku(String sku) {
        return productRepository.findBySku(sku);
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product updateProduct(Long id, Product product) {
        return productRepository.findById(id)
            .map(existing -> {
                existing.setName(product.getName());
                existing.setDescription(product.getDescription());
                existing.setPrice(product.getPrice());
                existing.setQuantity(product.getQuantity());
                existing.setSku(product.getSku());
                existing.setCategory(product.getCategory());
                return productRepository.save(existing);
            })
            .orElseThrow(() -> new RuntimeException("Product not found: " + id));
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public Product reserveInventory(Long productId, int quantity) {
        return productRepository.findById(productId)
            .map(product -> {
                if (product.getQuantity() < quantity) {
                    throw new RuntimeException(
                        "Insufficient inventory for product " + productId +
                        ": requested=" + quantity + ", available=" + product.getQuantity());
                }
                product.setQuantity(product.getQuantity() - quantity);
                return productRepository.save(product);
            })
            .orElseThrow(() -> new RuntimeException("Product not found: " + productId));
    }
}
