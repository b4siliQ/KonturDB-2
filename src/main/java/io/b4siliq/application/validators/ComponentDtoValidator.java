package io.b4siliq.application.validators;

import io.b4siliq.application.dtos.NewComponentDto;

public final class ComponentDtoValidator {
    private ComponentDtoValidator() {
        throw new UnsupportedOperationException("Validator class");
    }

    public static final void validate(NewComponentDto dto) {
        if (dto.name() == null || dto.name().isBlank())
            throw new IllegalArgumentException("Name of component cannot be null or empty!");

        if (dto.specification() == null || dto.specification().isBlank())
            throw new IllegalArgumentException("Specification of component cannot be null or empty");
    }
}
