package io.b4siliq.presentation;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.b4siliq.application.services.ComponentService;
import io.b4siliq.infrastructure.database.factories.DatabaseFactory;
import io.b4siliq.infrastructure.database.repositories.ComponentRepository;
import io.b4siliq.presentation.controllers.KonturMainController;
import io.b4siliq.presentation.utils.FxmlOrganizerUtil;

/**
 * JavaFX App
 */
public class App extends Application {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    private Scene scene;
    private ExecutorService executor;
    private ComponentRepository componentRepository;
    private ComponentService componentService;

    @Override
    public void start(Stage stage) throws IOException {
        try {
            var db = DatabaseFactory.createDatabase("sqlite");
            executor = Executors.newFixedThreadPool(2);
            this.componentRepository = new ComponentRepository(db, executor);
            this.componentService = new ComponentService(componentRepository);
        } catch(Exception e) {
            logger.error(
                "An error has occured while initializing repository and service:\n{}",
                e.getMessage()
            );
            throw new RuntimeException("Failed to initialize processes", e);
        }

        var mainSceneLoader = FxmlOrganizerUtil.getLoader(App.class, "KonturMain.fxml");
        Parent root = mainSceneLoader.load();
        KonturMainController mainSceneController = mainSceneLoader.getController();

        if (mainSceneController != null) mainSceneController.preapareController(this.componentService);

        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("KonturDB 2");
        stage.show();
    }

    @Override
    public void stop() {
        if (executor != null) executor.shutdown();
    }

    public static void main(String[] args) {
        launch();
    }

}