package com.cosmocats.marketplace.controller;

import com.cosmocats.marketplace.dto.ProductDTO;
import com.cosmocats.marketplace.service.ProductService;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerIT {
    private static final Long PRODUCT_ID = 1L;
    private static final String PRODUCT_NAME = "Star CocaCola";
    private static final String PRODUCT_NAME_UPDATED = "Updated Star CocaCola";
    private static final String INVALID_PRODUCT_NAME = "Invalid product name";
    private static final String PRODUCT_DESCRIPTION = "CocaCola is superb drink";
    private static final String PRODUCT_DESCRIPTION_UPDATED = "Updated CocaCola is superb drink";
    private static final double PRODUCT_PRICE = 1.99;

    private static final String PRODUCT_NAME_FIELD = "name";
    private static final String PRODUCT_DESCRIPTION_FIELD = "description";

    private static final String PRODUCT_NAME_VALIDATION_MESSAGE = "Validation failed for object 'productDTO': Field 'name': " +
            "Product name must contain a cosmic term (star, galaxy, comet)";

    private static final String PRODUCT_NOT_FOUND_MESSAGE = "Product with id 999 not found";
    private static final String PRODUCT_NOT_FOUND_TITLE = "Product Not Found";

    @LocalServerPort
    private int port;

    @Autowired
    private ProductService productService;

    private ProductDTO productDto;

    @BeforeEach
    void setUp() {
        productService.clear();

        productDto = ProductDTO.builder()
                .name(PRODUCT_NAME)
                .description(PRODUCT_DESCRIPTION)
                .price(PRODUCT_PRICE)
                .build();

        productService.createProduct(productDto);
    }

    @Test
    @DisplayName("Should create product and return 201 status")
    void testCreateProduct_validRequest() {
        ProductDTO productRequestDto = ProductDTO.builder()
                .name(PRODUCT_NAME)
                .description(PRODUCT_DESCRIPTION)
                .price(PRODUCT_PRICE)
                .build();

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(productRequestDto)
                .when()
                .post("/api/v1/products")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body(PRODUCT_NAME_FIELD, equalTo(productRequestDto.getName()))
                .body(PRODUCT_DESCRIPTION_FIELD, equalTo(productRequestDto.getDescription()));
    }

    @Test
    @DisplayName("Should return 400 when creating product with invalid name")
    void testCreateProduct_invalidRequest() {
        ProductDTO productRequestDto = ProductDTO.builder()
                .name(INVALID_PRODUCT_NAME)
                .description(PRODUCT_DESCRIPTION)
                .price(PRODUCT_PRICE)
                .build();

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(productRequestDto)
                .when()
                .post("/api/v1/products")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("title", equalTo("Validation Error"))
                .body("detail",  equalTo(PRODUCT_NAME_VALIDATION_MESSAGE));
    }

    @Test
    @DisplayName("Should return product by existing id")
    void testGetProductById_existingId() {

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .get("api/v1/products/{id}", PRODUCT_ID)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(PRODUCT_NAME_FIELD, equalTo(productDto.getName()))
                .body(PRODUCT_DESCRIPTION_FIELD, equalTo(productDto.getDescription()));
    }

    @Test
    @DisplayName("Should return 404 by non existing id")
    void testGetProductById_nonExistingId() {

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .get("api/v1/products/{id}", 999L)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", equalTo(PRODUCT_NOT_FOUND_TITLE))
                .body("detail",  equalTo(PRODUCT_NOT_FOUND_MESSAGE));
    }

    @Test
    @DisplayName("Should return all products")
    void testGetAllProducts() {
        // add one more product
        ProductDTO secondProductDto = ProductDTO.builder()
                .id(2L)
                .name("Second Star Product")
                .description("Second Star Product Description")
                .build();
        productService.createProduct(secondProductDto);

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/products")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(notNullValue())
                .body("size()", equalTo(2));
    }

    @Test
    @DisplayName("Should update product and return 200 status")
    void testUpdateProduct_existingId() {
        ProductDTO productUpdateDto = ProductDTO.builder()
                .name(PRODUCT_NAME_UPDATED)
                .description(PRODUCT_DESCRIPTION_UPDATED)
                .price(PRODUCT_PRICE)
                .build();

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(productUpdateDto)
                .when()
                .put("/api/v1/products/{id}", PRODUCT_ID)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(PRODUCT_NAME_FIELD, equalTo(productUpdateDto.getName()))
                .body(PRODUCT_DESCRIPTION_FIELD, equalTo(productUpdateDto.getDescription()));
    }

    @Test
    @DisplayName("Should return 404 status")
    void testUpdateProduct_nonExistingId() {
        ProductDTO productUpdateDto = ProductDTO.builder()
                .name(PRODUCT_NAME_UPDATED)
                .description(PRODUCT_DESCRIPTION_UPDATED)
                .price(PRODUCT_PRICE)
                .build();

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(productUpdateDto)
                .when()
                .put("/api/v1/products/{id}", 999L)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", equalTo(PRODUCT_NOT_FOUND_TITLE))
                .body("detail",  equalTo(PRODUCT_NOT_FOUND_MESSAGE));
    }

    @Test
    @DisplayName("Should delete product by existing id")
    void testDeleteProductById_existingId() {
        given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .delete("api/v1/products/{id}", PRODUCT_ID)
                .then()
                .log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());

        given()
                .port(port)
                .when()
                .get("/api/v1/products/{id}", PRODUCT_ID)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("Should return 204 while deleting product by non existing id")
    void deleteProduct_invalidId_shouldReturnNotFound() {

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .delete("api/v1/products/{id}", 999L)
                .then()
                .log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}