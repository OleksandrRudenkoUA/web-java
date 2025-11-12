package com.cosmocats.marketplace.controller;

import com.cosmocats.marketplace.dto.ProductDTO;
import com.cosmocats.marketplace.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return service.getAllProducts();
    }

    @GetMapping("/{productId}")
    public ProductDTO getProductById(@PathVariable Long productId) {
        return service.getProductById(productId);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createProduct(dto));
    }

    @PutMapping("/{productId}")
    public ProductDTO updateProductById(@PathVariable Long productId, @Valid @RequestBody ProductDTO dto) {
        return service.updateProductById(productId, dto);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long productId) {
        service.deleteProductById(productId);  // Idempotent: завжди 204
        return ResponseEntity.noContent().build();
    }
}