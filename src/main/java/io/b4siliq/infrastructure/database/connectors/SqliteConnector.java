package io.b4siliq.infrastructure.database.connectors;

import java.io.File;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import io.b4siliq.infrastructure.database.contracts.DatabaseConnector;
import io.b4siliq.infrastructure.database.exceptions.FolderCreationException;

public class SqliteConnector implements DatabaseConnector {
    private final Sql2o sql2o;
    private static final Logger logger = LoggerFactory.getLogger(SqliteConnector.class);

    public SqliteConnector() throws FolderCreationException {
        var url = "jdbc:sqlite:" + this.getDBPath();
        this.sql2o = new Sql2o(url, null, null);
    }

    @Override
    public Connection open() {
        return this.sql2o.open();
    }

    @Override
    public Connection openTransactional() {
        return this.sql2o.beginTransaction();
    }

    @Override
    public Sql2o getSql2o() {
        return this.sql2o;
    }

    private String getDBPath() throws FolderCreationException {
        var propertyPath = System.getProperty("user.home");
        var companyFolderName = ".KonturSolutions";
        var applicationFolderName = "KonturDB";
        var fileName = "KonturDB.db";

        File applicationDir = Paths.get(propertyPath, companyFolderName, applicationFolderName).toFile();

        if (!applicationDir.exists()) {
            if (applicationDir.mkdirs()) {
                logger.info("Database folder created!");
            } else {
                logger.error("Cannot create database folder");
                throw new FolderCreationException("Database folder creation error");
            }
        }

        return Paths.get(applicationDir.getAbsolutePath(), fileName).toString();
    }
}
