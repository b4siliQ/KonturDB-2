package io.b4siliq.infrastructure.database.repositories;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.b4siliq.application.contracts.SearchColumnEnum;
import io.b4siliq.application.dtos.NewComponentDto;
import io.b4siliq.application.dtos.UpdatedComponentDto;
import io.b4siliq.domain.entities.Component;
import io.b4siliq.infrastructure.database.abstracts.GenericRepository;
import io.b4siliq.infrastructure.database.contracts.DatabaseConnector;
import io.b4siliq.infrastructure.database.exceptions.TableOperationException;

public class ComponentRepository extends GenericRepository<Component> {
    private static final Logger logger = LoggerFactory.getLogger(ComponentRepository.class);

    public ComponentRepository(DatabaseConnector db, Executor executor) {
        super(db, executor);
    }

    @Override
    public CompletableFuture<Void> createTableAsync() {
        return CompletableFuture.runAsync(() -> {
            var request = """
                    CREATE TABLE IF NOT EXISTS Components (
                        id TEXT PRIMARY KEY,
                        name TEXT NOT NULL,
                        specification TEXT NOT NULL,
                        price REAL,
                        quantity INTEGER,
                        box TEXT,
                        description TEXT,
                        datasheet TEXT,
                        thumbnail TEXT,
                        favorite INTEGER DEFAULT 0
                    )
                    """;
            try(var con = super.getDatabase().open()) {
                logger.info("Creating component table...");
                con.createQuery(request).executeUpdate();
            } catch(Exception e) {
                logger.error(
                    "An error has occured while creating component table:\n{}",
                    e
                );
                throw new TableOperationException("Failed to create table", e);
            }
        }, super.getExecutor());
    }

    @Override
    public CompletableFuture<Void> destroyTableAsync() {
        return CompletableFuture.runAsync(() -> {
            var request = """
                    DROP TABLE IF EXISTS Components
                    """;
            try(var con = super.getDatabase().open()) {
                logger.info("Destroying component table...");
                con.createQuery(request).executeUpdate();
            } catch (Exception e) {
                logger.error(
                    "An error has occured while destroying component table:\n{}",
                    e
                );
                throw new TableOperationException("Failed to destroy table", e);
            }
        }, super.getExecutor());
    }

    @Override
    public CompletableFuture<Collection<Component>> getAllAsync() {
        return CompletableFuture.supplyAsync(() -> {
            var request = """
                    SELECT *
                    FROM Components
                    ORDER BY name
                    """;
            try(var con = super.getDatabase().open()) {
                logger.info("Fetching all data from component table...");
                return con.createQuery(request).executeAndFetch(Component.class);
            } catch(Exception e) {
                logger.error(
                    "An error has occured while fetching all data from component table:\n{}",
                    e
                );
                throw new TableOperationException("Failed to fetch all data from table", e);
            }
        }, super.getExecutor());
    }

    public CompletableFuture<Void> addAsync(NewComponentDto dto) {
        return CompletableFuture.runAsync(() -> {
            var request = """
                    INSERT INTO Components (
                        id,
                        name,
                        specification,
                        price,
                        quantity
                    )
                    VALUES (:id, :name, :specification, 0, 0)
                    """;
            try(var con = super.getDatabase().open()) {
                logger.info("Inserting component from dto into table...");
                con.createQuery(request)
                    .addParameter("id", UUID.randomUUID().toString())
                    .addParameter("name", dto.name())
                    .addParameter("specification", dto.specification())
                    .executeUpdate();
            } catch(Exception e) {
                logger.error(
                    "An error has occured while inserting new component from dto into table:\n{}",
                    e
                );
                throw new TableOperationException("Failed to insert data into table", e);
            }
        }, super.getExecutor());
    }

    @Override
    public CompletableFuture<Void> addAsync(Component entity) {
        return CompletableFuture.runAsync(() -> {
            var request = """
                    INSERT INTO Components (
                        id,
                        name,
                        specification,
                        price,
                        quantity,
                        box,
                        description,
                        datasheet,
                        thumbnail
                    ) VALUES(
                        :id,
                        :name,
                        :specification,
                        :price,
                        :quantity,
                        :box,
                        :description,
                        :datasheet,
                        :thumbnail
                    )
                    """;
            try(var con = super.getDatabase().open()) {
                logger.info("Inserting component from component object into table...");
                con.createQuery(request)
                    .addParameter("id", UUID.randomUUID().toString())
                    .addParameter("name", entity.name)
                    .addParameter("specification", entity.specification)
                    .addParameter("price", entity.price)
                    .addParameter("quantity", entity.quantity)
                    .addParameter("box", entity.box)
                    .addParameter("description", entity.description)
                    .addParameter("datasheet", entity.datasheet)
                    .addParameter("thumbnail", entity.thumbnail)
                    .executeUpdate();
            } catch(Exception e) {
                logger.error(
                    "An error has occured while inserting new component from entity into table:\n{}",
                    e
                );
                throw new TableOperationException("Failed to insert data into table", e);
            }
        }, super.getExecutor());
    }

    @Override
    public CompletableFuture<Void> addRangeAsync(Collection<Component> range) {
        return CompletableFuture.runAsync(() -> {
            var request = """
                    INSERT INTO Components (
                        id,
                        name,
                        specification,
                        price,
                        quantity,
                        box,
                        description,
                        datasheet,
                        thumbnail
                    ) VALUES(
                        :id,
                        :name,
                        :specification,
                        :price,
                        :quantity,
                        :box,
                        :description,
                        :datasheet,
                        :thumbnail
                    )
                    """;
            try(var con = super.getDatabase().openTransactional()) {
                for (var entity : range) {
                    logger.info("Inserting component from component object into table...");
                    con.createQuery(request)
                        .addParameter("id", UUID.randomUUID().toString())
                        .addParameter("name", entity.name)
                        .addParameter("specification", entity.specification)
                        .addParameter("price", entity.price)
                        .addParameter("quantity", entity.quantity)
                        .addParameter("box", entity.box)
                        .addParameter("description", entity.description)
                        .addParameter("datasheet", entity.datasheet)
                        .addParameter("thumbnail", entity.thumbnail)
                        .executeUpdate();
                }
                con.commit();
            } catch(Exception e) {
                logger.error(
                    "An error has occured while inserting new component from dto into table:\n{}",
                    e
                );
                throw new TableOperationException("Failed to insert data into table", e);
            }
        }, super.getExecutor());
    }

    public CompletableFuture<Void> addRangeByDtoAsync(Collection<NewComponentDto> range) {
        return CompletableFuture.runAsync(() -> {
            var request = """
                    INSERT INTO Components (
                        id,
                        name,
                        specification,
                        price,
                        quantity
                    )
                    VALUES (:id, :name, :specification, 0, 0)
                    """;
            try(var con = super.getDatabase().openTransactional()) {
                for (var dto : range) {
                    logger.info("Inserting component from dto into table...");
                    con.createQuery(request)
                        .addParameter("id", UUID.randomUUID().toString())
                        .addParameter("name", dto.name())
                        .addParameter("specification", dto.specification())

                        .executeUpdate();
                }
                con.commit();
            } catch(Exception e) {
                logger.error(
                    "An error has occured while inserting new component from dto into table:\n{}",
                    e
                );
                throw new TableOperationException("Failed to insert data into table", e);
            }
        }, super.getExecutor());
    }

    @Override
    public CompletableFuture<Component> getFirstOrDefaultAsync(UUID id) {
        return CompletableFuture.supplyAsync(() -> {
            var request = """
                    SELECT *
                    FROM Components
                    WHERE id = :id
                    LIMIT 1
                    """;
            try(var con = super.getDatabase().open()) {
                logger.info("Fetching single component from table...");
                return con.createQuery(request)
                    .addParameter("id", id.toString())
                    .executeAndFetchFirst(Component.class);
            } catch(Exception e) {
                logger.error(
                    "An error has occured while fetching single component from table:\n{}",
                    e
                );
                throw new TableOperationException("Failed to fetch single data from table", e);
            }
        }, super.getExecutor());
    }

    @Override
    public CompletableFuture<Component> getUniqueFirstOrDefaultAsync(UUID id) {
        return CompletableFuture.supplyAsync(() -> {
            var request = """
                    SELECT *
                    FROM Components
                    WHERE id = :id
                    """;
            try(var con = super.getDatabase().open()) {
                logger.info("Fetching single unique component from table...");
                return con.createQuery(request)
                    .addParameter("id", id.toString())
                    .executeAndFetchUnique(Component.class);
            } catch(Exception e) {
                logger.error(
                    "An error has occured while fetching unique single component from table:\n{}",
                    e
                );
                throw new TableOperationException("Failed to fetch unique singel data from table", e);
            }
        }, super.getExecutor());
    }

    @Override
    public CompletableFuture<Void> updateAsync(Component entity) {
        return CompletableFuture.runAsync(() -> {
            var request = """
                    UPDATE Components
                    SET
                        name = :name,
                        specification = :specification,
                        price = :price,
                        quantity = :quantity,
                        box = :box,
                        description = :description,
                        datasheet = :datasheet,
                        thumbnail = :thumbnail
                    WHERE id = :id
                    """;
            try(var con = super.getDatabase().open()) {
                logger.info("Updating data of component in table...");
                con.createQuery(request)
                    .addParameter("name", entity.name)
                    .addParameter("specification", entity.specification)
                    .addParameter("price", entity.price)
                    .addParameter("quantity", entity.quantity)
                    .addParameter("box", entity.box)
                    .addParameter("description", entity.description)
                    .addParameter("datasheet", entity.datasheet)
                    .addParameter("thumbnail", entity.thumbnail)
                    .addParameter("id", entity.id)
                    .executeUpdate();
            } catch(Exception e) {
                logger.error(
                    "An error has occured while updating data of component in table:\n{}",
                    e
                );
                throw new TableOperationException("Failed to update data of component", e);
            }
        }, super.getExecutor());
    }

    public CompletableFuture<Void> updateAsync(UpdatedComponentDto dto) {
        return CompletableFuture.runAsync(() -> {
            var request = """
                    UPDATE Components
                    SET
                        name = :name,
                        specification = :specification,
                        price = :price,
                        quantity = :quantity,
                        box = :box,
                        description = :description,
                        datasheet = :datasheet,
                        thumbnail = :thumbnail
                    WHERE id = :id
                    """;
            try(var con = super.getDatabase().open()) {
                logger.info("Updating data of component in table by updated dto...");
                con.createQuery(request)
                    .addParameter("name", dto.name())
                    .addParameter("specification", dto.spec())
                    .addParameter("price", dto.price())
                    .addParameter("quantity", dto.quantity())
                    .addParameter("box", dto.box())
                    .addParameter("description", dto.desc())
                    .addParameter("datasheet", dto.datasheet())
                    .addParameter("thumbnail", dto.thumbnail())
                    .addParameter("id", dto.id())
                    .executeUpdate();
            } catch(Exception e) {
                logger.error(
                    "An error has occured while updating data of component in table:\n{}",
                    e
                );
                throw new TableOperationException("Failed to update data of component", e);
            }
        }, super.getExecutor());
    }

    @Override
    public CompletableFuture<Void> updateRangeAsync(Collection<Component> range) {
        return CompletableFuture.runAsync(() -> {
            var request = """
                    UPDATE Components
                    SET
                        name = :name,
                        specification = :specification,
                        price = :price,
                        quantity = :quantity,
                        box = :box,
                        description = :description,
                        datasheet = :datasheet
                    WHERE id = :id
                    """;
            try(var con = super.getDatabase().openTransactional()) {
                for (var entity : range) {
                    logger.info("Updating data of component in table...");
                    con.createQuery(request)
                        .addParameter("name", entity.name)
                        .addParameter("specification", entity.specification)
                        .addParameter("price", entity.price)
                        .addParameter("quantity", entity.quantity)
                        .addParameter("box", entity.box)
                        .addParameter("description", entity.description)
                        .addParameter("datasheet", entity.datasheet)
                        .addParameter("id", entity.id)
                        .executeUpdate();
                }
                con.commit();
            } catch(Exception e) {
                logger.error(
                    "An error has occured while updating data of component in table:\n{}",
                    e
                );
                throw new TableOperationException("Failed to update data of component", e);
            }
        }, super.getExecutor());
    }

    @Override
    public CompletableFuture<Collection<Component>> searchAsync(SearchColumnEnum column, String searchTerm) {
        return CompletableFuture.supplyAsync(() -> {
            var request = String.format("""
                    SELECT *
                    FROM Components
                    WHERE %s LIKE :search
                    """, column.toString());
            try(var con = super.getDatabase().open()) {
                logger.info("Searching for components data in table...");
                return con.createQuery(request)
                    .addParameter("search", "%"+searchTerm+"%")
                    .executeAndFetch(Component.class);
            } catch(Exception e) {
                logger.error(
                    "An error has occured while searching for components data in table:\n{}",
                    e
                );
                throw new TableOperationException("Failed to search data", e);
            }
        }, super.getExecutor());
    }

    @Override
    public CompletableFuture<Void> deleteAsync(UUID id) {
        return CompletableFuture.runAsync(() -> {
            var request = """
                    DELETE FROM Components
                    WHERE id = :id
                    """;
            try(var con = super.getDatabase().open()) {
                logger.info("Deleting component from table...");
                con.createQuery(request)
                    .addParameter("id", id.toString())
                    .executeUpdate();
            } catch(Exception e) {
                logger.error(
                    "An error has occured while deleting component from table:\n{}",
                    e
                );
                throw new TableOperationException("Failed to delete a component", e);
            }
        }, super.getExecutor());
    }

    @Override
    public CompletableFuture<Void> deleteRangeAsync(Collection<Component> range) {
        return CompletableFuture.runAsync(() -> {
            var request = """
                    DELETE FROM Components
                    WHERE id = :id
                    """;
            try(var con = super.getDatabase().openTransactional()) {
                for (var entity : range) {
                    logger.info("Deleting component from table...");
                    con.createQuery(request)
                        .addParameter("id", entity.id)
                        .executeUpdate();
                }
                con.commit();
            } catch(Exception e) {
                logger.error(
                    "An error has occured while deleting component from table:\n{}",
                    e
                );
                throw new TableOperationException("Failed to delete a component", e);
            }
        }, super.getExecutor());
    }

    public CompletableFuture<Void> setFavorite(UUID id, boolean favorite) {
        int comparableInt = favorite ? 0 : 1;
        return CompletableFuture.runAsync(() -> {
            var request = """
                    UPDATE Components
                    SET
                        favorite = :favorite
                    WHERE
                    id = :id
                    """;
            try(var con = super.getDatabase().open()) {
                logger.info("Setting component to favorites...");
                con.createQuery(request)
                    .addParameter("favorite", comparableInt)
                    .addParameter("id", id.toString())
                    .executeUpdate();
            } catch(Exception e) {
                logger.error(
                    "An error has occured while setting component to favorites:\n{}",
                    e
                );
                throw new TableOperationException("Failed to set to favorites", e);
            }
        }, super.getExecutor());
    }
}
