package com.cosmocats.marketplace.service;

import com.cosmocats.marketplace.domain.Product;
import com.cosmocats.marketplace.dto.ProductDTO;
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

    public List<ProductDTO> getProducts() {
        return products.stream().map(mapper::toDTO).toList();
    }

    public Optional<ProductDTO> getProductById(Long id) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .map(mapper::toDTO)
                .findFirst();
    }

    public Optional<ProductDTO> updateProduct(Long id, ProductDTO dto) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .map(p -> {
                    p.setName(dto.getName());
                    p.setDescription(dto.getDescription());
                    p.setPrice(dto.getPrice());
                    // Update category if needed
                    return mapper.toDTO(p);
                });
    }

    public boolean deleteProduct(Long id) {
        return products.removeIf(p -> p.getId().equals(id));
    }
}