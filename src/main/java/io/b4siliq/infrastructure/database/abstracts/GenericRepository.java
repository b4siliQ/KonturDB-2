package io.b4siliq.infrastructure.database.abstracts;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import io.b4siliq.application.dtos.NewComponentDto;
import io.b4siliq.application.dtos.UpdatedComponentDto;
import io.b4siliq.infrastructure.database.contracts.DatabaseConnector;
import io.b4siliq.infrastructure.database.enums.SearchColumnEnum;

public abstract class GenericRepository<T> {
    private final DatabaseConnector db;
    private final Executor executor;

    public GenericRepository(DatabaseConnector db, Executor executor) {
        this.db = db;
        this.executor = executor;
    }

    public abstract CompletableFuture<Void> createTableAsync();
    public abstract CompletableFuture<Void> destroyTableAsync();

    public abstract CompletableFuture<Collection<T>> getAllAsync();
    public abstract CompletableFuture<Void> addAsync(NewComponentDto dto);
    public abstract CompletableFuture<Void> addAsync(T entity);
    public abstract CompletableFuture<Void> addRangeAsync(Collection<T> range);
    public abstract CompletableFuture<Void> addRangeByDtoAsync(Collection<NewComponentDto> range);
    public abstract CompletableFuture<T> getFirstOrDefaultAsync(UUID id);
    public abstract CompletableFuture<T> getUniqueFirstOrDefaultAsync(UUID id);
    public abstract CompletableFuture<Void> updateAsync(T entity);
    public abstract CompletableFuture<Void> updateAsync(UpdatedComponentDto dto);
    public abstract CompletableFuture<Void> updateRangeAsync(Collection<T> range);
    public abstract CompletableFuture<Collection<T>> searchAsync(SearchColumnEnum column, String searchTerm);
    public abstract CompletableFuture<Void> deleteAsync(UUID id);
    public abstract CompletableFuture<Void> deleteRangeAsync(Collection<T> range);

    protected DatabaseConnector getDatabase() {
        return this.db;
    }

    public Executor getExecutor() {
        return this.executor;
    }
}
