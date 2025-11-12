package com.cosmocats.marketplace.service;

import com.cosmocats.marketplace.domain.Product;
import com.cosmocats.marketplace.dto.ProductDTO;
import com.cosmocats.marketplace.exception.ProductNotFoundException;
import com.cosmocats.marketplace.mapper.ProductMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProductService {
    private final List<Product> products = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong();
    private final ProductMapper mapper;

    public ProductService(ProductMapper mapper) {
        this.mapper = mapper;
    }

    public ProductDTO createProduct(ProductDTO dto) {
        Product product = mapper.toEntity(dto);
        product.setId(counter.incrementAndGet());
        products.add(product);
        return mapper.toDTO(product);
    }

    public List<ProductDTO> getAllProducts() {
        return products.stream().map(mapper::toDTO).toList();
    }

    public ProductDTO getProductById(Long productId) {
        return products.stream()
                .filter(p -> p.getId().equals(productId))
                .map(mapper::toDTO)
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    public ProductDTO updateProductById(Long productId, ProductDTO dto) {
        Optional<Product> existingProduct = products.stream()
                .filter(p -> p.getId().equals(productId))
                .findFirst();
        if (existingProduct.isPresent()) {
            Product updatedProduct = mapper.toEntity(dto);
            updatedProduct.setId(productId);
            products.removeIf(p -> p.getId().equals(productId));
            products.add(updatedProduct);
            return mapper.toDTO(updatedProduct);
        } else {
            throw new ProductNotFoundException(productId);
        }
    }

    public void deleteProductById(Long productId) {
        products.removeIf(p -> p.getId().equals(productId));
    }
}