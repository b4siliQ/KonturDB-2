package io.b4siliq.infrastructure.database.contracts;

import org.sql2o.Connection;
import org.sql2o.Sql2o;

public interface DatabaseConnector {
    Connection open();
    Connection openTransactional();
    Sql2o getSql2o();
}
