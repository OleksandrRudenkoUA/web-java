package com.cosmocats.marketplace.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CosmicWordValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CosmicWordCheck {
    String message() default "Name must contain cosmic terms like 'star', 'galaxy', or 'comet'";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}