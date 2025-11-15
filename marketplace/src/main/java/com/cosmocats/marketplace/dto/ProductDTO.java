package com.cosmocats.marketplace.dto;

import com.cosmocats.marketplace.validation.CosmicWordCheck;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDTO {
    private Long id;

    @NotNull(message = "Name is required")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    @CosmicWordCheck
    private String name;

    @Size(max = 500, message = "Description must be up to 500 characters")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than or equal to 0.01")
    private double price;

    private Long categoryId;
}
