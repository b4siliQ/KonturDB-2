package io.b4siliq.infrastructure.database.abstracts;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import io.b4siliq.application.contracts.SearchColumnEnum;
import io.b4siliq.infrastructure.database.contracts.DatabaseConnector;

/**
 * Абстрактный класс, реализующий паттерн "Обобщенный репозиторий".
 * Требует экземпляр коннектора к базе данных и экзекутор для асинхронных вызовов
 * @author b4siliQ
*/
public abstract class GenericRepository<T> {
    private final DatabaseConnector db;
    private final Executor executor;

    public GenericRepository(DatabaseConnector db, Executor executor) {
        this.db = db;
        this.executor = executor;
    }

    /**
     * Метод для создания таблицы
     * @return CompletableFuture<Void> - асинхронный вызов процесса
    */
    public abstract CompletableFuture<Void> createTableAsync();
    /**
     * Метод для удаления таблицы
     * @return CompletableFuture - асинхронный вызов процесса
    */
    public abstract CompletableFuture<Void> destroyTableAsync();

    /**
     * Метод для получения всех элементов таблицы в коллекцию из {@link T} экземпляров
     * @return CompletableFuture - асинхронный вызов процесса
    */
    public abstract CompletableFuture<Collection<T>> getAllAsync();
    /**
     * Метод для добавления {@link T} экземпляра в таблицу
     * @return CompletableFuture - асинхронный вызов процесса
    */
    public abstract CompletableFuture<Void> addAsync(T entity);
    /**
     * Метод для добавления нескольких {@link T} экземпляров в таблицу
     * @return CompletableFuture - асинхронный вызов процесса
    */
    public abstract CompletableFuture<Void> addRangeAsync(Collection<T> range);
    /**
     * Метод для получения {@link T} экземпляра из таблицы
     * @return CompletableFuture - асинхронный вызов процесса
    */
    public abstract CompletableFuture<T> getFirstOrDefaultAsync(UUID id);
    /**
     * Метод для получения уникального {@link T} экземпляра из таблицы
     * @return CompletableFuture - асинхронный вызов процесса
    */
    public abstract CompletableFuture<T> getUniqueFirstOrDefaultAsync(UUID id);
    /**
     * Метод для редактирования элемента таблицы
     * @return CompletableFuture - асинхронный вызов процесса
    */
    public abstract CompletableFuture<Void> updateAsync(T entity);
    /**
     * Метод для редактирования нескольких элементов в таблице
     * @return CompletableFuture - асинхронный вызов процесса
    */
    public abstract CompletableFuture<Void> updateRangeAsync(Collection<T> range);
    /**
     * Метод для поиска элементов в таблице
     * @return CompletableFuture - асинхронный вызов процесса
    */
    public abstract CompletableFuture<Collection<T>> searchAsync(SearchColumnEnum column, String searchTerm);
    /**
     * Метод для удаления элемента из таблице
     * @return CompletableFuture - асинхронный вызов процесса
    */
    public abstract CompletableFuture<Void> deleteAsync(UUID id);
    /**
     * Метод для удаления нескольких элементов из таблицы
     * @return CompletableFuture - асинхронный вызов процесса
    */
    public abstract CompletableFuture<Void> deleteRangeAsync(Collection<T> range);

    /**
     * Метод для получения объекта базы данных из коннектора
     * @return {@link DatabaseConnector} - коннектор базы данных
    */
    protected DatabaseConnector getDatabase() {
        return this.db;
    }

    /**
     * Метод для получения действующего исполнителя репозитория
     * @return {@link Executor} - исполнитель
    */
    public Executor getExecutor() {
        return this.executor;
    }
}
