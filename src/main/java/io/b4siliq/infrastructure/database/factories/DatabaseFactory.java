package io.b4siliq.infrastructure.database.factories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.b4siliq.infrastructure.database.connectors.SqliteConnector;
import io.b4siliq.infrastructure.database.contracts.DatabaseConnector;
import io.b4siliq.infrastructure.database.exceptions.UnsupportedDatabaseFactoryTypeException;

public final class DatabaseFactory {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseFactory.class);

    private DatabaseFactory() {
        throw new UnsupportedOperationException("Factory class");
    }

    public static final DatabaseConnector createDatabase(String dbType) throws Exception {
        return switch(dbType.toLowerCase()) {
            case "sqlite" -> new SqliteConnector();
            default -> {
                logger.error("Cannot create database by this type");
                throw new UnsupportedDatabaseFactoryTypeException("Unknown database type");
            }
        };
    }
}
