package io.b4siliq.application.services;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import io.b4siliq.application.dtos.NewComponentDto;
import io.b4siliq.application.dtos.UpdatedComponentDto;
import io.b4siliq.application.enums.SearchComponentColumnEnum;
import io.b4siliq.application.validators.ComponentDtoValidator;
import io.b4siliq.application.validators.ComponentValidator;
import io.b4siliq.application.validators.UpdatedComponentDtoValidator;
import io.b4siliq.domain.entities.Component;
import io.b4siliq.infrastructure.database.repositories.ComponentRepository;

public class ComponentService {
    private final ComponentRepository repo;

    public ComponentService(ComponentRepository repo) {
        this.repo = repo;
    }

    public CompletableFuture<Void> initializeDB() {
        return this.repo.createTableAsync();
    }

    public CompletableFuture<Collection<Component>> getAllComponents() {
        return this.repo.getAllAsync();
    }

    public CompletableFuture<Void> createNewComponent(NewComponentDto dto) {
        ComponentDtoValidator.validate(dto);
        return this.repo.addAsync(dto);
    }

    public CompletableFuture<Void> createNewComponent(Component component) {
        ComponentValidator.validate(component);
        return this.repo.addAsync(component);
    }

    public CompletableFuture<Component> getComponent(UUID id) {
        return this.repo.getFirstOrDefaultAsync(id);
    }

    public CompletableFuture<Void> deleteComponent(UUID id) {
        return this.repo.deleteAsync(id);
    }

    public CompletableFuture<Void> updateComponent(UpdatedComponentDto dto) {
        UpdatedComponentDtoValidator.validate(dto);
        return this.repo.updateAsync(dto);
    }

    public CompletableFuture<Collection<Component>> searchService(SearchComponentColumnEnum column, String searchTerm) {
        return this.repo.searchAsync(column, searchTerm);
    }

    public CompletableFuture<Void> setComponentToFavorite(UUID id, boolean favorite) {
        return this.repo.setFavorite(id, favorite);
    }
}
