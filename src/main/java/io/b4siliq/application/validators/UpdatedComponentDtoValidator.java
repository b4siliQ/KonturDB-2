package io.b4siliq.application.validators;

import io.b4siliq.application.dtos.UpdatedComponentDto;

public final class UpdatedComponentDtoValidator {
    private UpdatedComponentDtoValidator() {
        throw new UnsupportedOperationException("Validator class");
    }

    public static final void validate(UpdatedComponentDto body) {
        if (body.name() == null || body.name().isBlank())
            throw new IllegalArgumentException("Name of component cannot be null or empty");

        if (body.spec() == null || body.spec().isBlank())
            throw new IllegalArgumentException("Specification of component cannot be null or empty");

        if (body.price() < 0)
            throw new IllegalArgumentException("Price of component cannot be negative");

        if (body.quantity() < 0)
            throw new IllegalArgumentException("Quantity of component cannot be negative");
    }
}
