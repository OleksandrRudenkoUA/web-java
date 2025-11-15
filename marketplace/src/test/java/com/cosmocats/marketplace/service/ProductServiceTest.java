package com.cosmocats.marketplace.service;

import com.cosmocats.marketplace.config.MappersTestConfiguration;
import com.cosmocats.marketplace.domain.Category;
import com.cosmocats.marketplace.domain.Product;
import com.cosmocats.marketplace.dto.ProductDTO;
import com.cosmocats.marketplace.exception.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ProductService.class)
@Import(MappersTestConfiguration.class)
@DisplayName("Product service test")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    private static final Long CATEGORY_ID = 100L;
    private static final String CATEGORY_NAME = "Drinks";

    private static final Long PRODUCT_ID = 1L;
    private static final String PRODUCT_NAME = "Star CocaCola";
    private static final String PRODUCT_DESCRIPTION = "CocaCola is superb drink";
    private static final double PRODUCT_PRICE = 1.99;

    private Product testProduct;
    private Category testCategory;
    private ProductDTO productDto;

    @Autowired
    private ProductService productService;


    @BeforeEach
    void setUp() {
        testCategory = Category.builder()
                .id(CATEGORY_ID)
                .name(CATEGORY_NAME)
                .build();

        testProduct = Product.builder()
                .id(PRODUCT_ID)
                .name(PRODUCT_NAME)
                .description(PRODUCT_DESCRIPTION)
                .category(testCategory)
                .price(PRODUCT_PRICE)
                .build();

        productDto = ProductDTO.builder()
                .name(PRODUCT_NAME)
                .description(PRODUCT_DESCRIPTION)
                .price(PRODUCT_PRICE)
                .build();
    }

    @Test
    @DisplayName("Should create product successfully")
    void testCreateProduct() {
        ProductDTO created = productService.createProduct(productDto);

        assertNotNull(created);
        assertEquals(PRODUCT_ID, created.getId());
        assertEquals(PRODUCT_NAME, created.getName());
        assertEquals(PRODUCT_DESCRIPTION, created.getDescription());
        assertEquals(PRODUCT_PRICE, created.getPrice());

    }

    @Test
    @DisplayName("Should return empty list when no products exist")
    void testGetAllProductsEmpty() {
        List<ProductDTO> result = productService.getAllProducts();

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should get product by existing id")
    void testGetExistingProductById() {
        productService.clear();
        productService.createProduct(productDto);

        ProductDTO result = productService.getProductById(PRODUCT_ID);

        assertNotNull(result);
        assertEquals(PRODUCT_ID, result.getId());
    }

    @Test
    @DisplayName("Should throw exception for non-existing id")
    void testGetNonExistingProductById() {
        ProductNotFoundException ex = assertThrows(
                ProductNotFoundException.class,
                () -> productService.getProductById(999L)
        );

        assertEquals("Product with id 999 not found", ex.getMessage());
    }

    @Test
    @DisplayName("Should update product successfully")
    void testUpdateProduct() {
        productService.createProduct(productDto);
        String updatedName = "Updated star CocaCola";
        String updatedDescription = "Updated CocaCola is superb drink";

        ProductDTO updatedDto = ProductDTO.builder()
                .name(updatedName)
                .description(updatedDescription)
                .build();

        ProductDTO result = productService.updateProductById(1L, updatedDto);

        assertNotNull(result);
        assertEquals(updatedName, result.getName());
        assertEquals(updatedDescription, result.getDescription());
    }

    @Test
    @DisplayName("Should throw exception when updating non-existing product")
    void testUpdateNonExistingProduct() {
        ProductDTO updateDto = ProductDTO.builder()
                .name("star name")
                .description("description")
                .build();

        ProductNotFoundException ex = assertThrows(
                ProductNotFoundException.class,
                () -> productService.updateProductById(123L, updateDto)
        );

        assertEquals("Product with id 123 not found", ex.getMessage());
    }

    @Test
    @DisplayName("Should return all products")
    void testGetAllProducts() {
        productService.clear();
        ProductDTO firstProduct = productService.createProduct(productDto);
        ProductDTO secondProduct = productService.createProduct(productDto);

        List<ProductDTO> result = productService.getAllProducts();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Should delete product successfully")
    void testDeleteProduct() {
        productService.clear();
        productService.createProduct(productDto);

        productService.deleteProductById(1L);

        assertTrue(productService.getAllProducts().isEmpty());
    }
}