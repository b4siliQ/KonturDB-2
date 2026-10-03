package io.b4siliq.application.validators;

import io.b4siliq.domain.entities.Component;

public final class ComponentValidator {
    private ComponentValidator() {
        throw new UnsupportedOperationException("Validator class");
    }

    public static final void validate(Component body) {
        if (body.name == null || body.name.isBlank())
            throw new IllegalArgumentException("Name of component cannot be null or empty");

        if (body.specification == null || body.specification.isBlank())
            throw new IllegalArgumentException("Specification of component cannot be null or empty");

        if (body.price.doubleValue() < 0)
            throw new IllegalArgumentException("Price of component cannot be negative");

        if (body.quantity < 0)
            throw new IllegalArgumentException("Quantity of component cannot be negative");
    }
}
