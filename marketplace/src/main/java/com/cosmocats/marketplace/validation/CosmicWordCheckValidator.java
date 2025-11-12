package com.cosmocats.marketplace.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class CosmicWordCheckValidator implements ConstraintValidator<CosmicWordCheck, String> {
    private static final List<String> COSMIC_TERMS = Arrays.asList("star", "galaxy", "comet");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return COSMIC_TERMS.stream().anyMatch(term -> value.toLowerCase().contains(term));
    }
}