module io.b4siliq {
    requires javafx.controls;
    requires javafx.fxml;
    requires sql2o;
    requires java.sql;
    requires org.slf4j;
    requires ch.qos.logback.classic;
    requires javafx.graphics;
    requires java.desktop;
    requires javafx.base;

    opens io.b4siliq.presentation.controllers to javafx.fxml;
    opens io.b4siliq.presentation.models to javafx.fxml;
    opens io.b4siliq.domain.entities to sql2o;
    // opens io.b4siliq.infrastructure.database.contracts to sql2o;
    // opens io.b4siliq.infrastructure.database.connectors to sql2o;

    exports io.b4siliq.presentation;
}
