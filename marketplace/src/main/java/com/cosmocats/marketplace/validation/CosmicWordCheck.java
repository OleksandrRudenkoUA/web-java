package com.cosmocats.marketplace.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CosmicWordCheckValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CosmicWordCheck {
    String message() default "Product name must contain a cosmic term (star, galaxy, comet)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}