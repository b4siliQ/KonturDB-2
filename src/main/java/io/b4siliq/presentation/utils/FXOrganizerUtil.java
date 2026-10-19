package io.b4siliq.presentation.utils;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public final class FXOrganizerUtil {
    private static final Logger logger = LoggerFactory.getLogger(FXOrganizerUtil.class);

    private FXOrganizerUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static final Parent loadFxml(Class<?> currentClass, String fxmlName) throws IOException {
        var resource = currentClass.getResource("/io/b4siliq/faces/" + fxmlName);
        var loader = new FXMLLoader(resource);

        return loader.load();
    }

    public static final <T> T loadController(Class<?> currentClass, String fxmlName) throws IOException {
        var resource = currentClass.getResource("/io/b4siliq/faces/" + fxmlName);
        var loader = new FXMLLoader(resource);

        return loader.getController();
    }

    public static final FXMLLoader getLoader(Class<?> currentClass, String fxmlName) throws IOException {
        var resource = currentClass.getResource("/io/b4siliq/faces/" + fxmlName);
        return new FXMLLoader(resource);
    }

    public static final void setIcon(Stage stage, String name) {
        try {
            var icon = new Image(FXOrganizerUtil.class.getResourceAsStream("/io/b4siliq/icons/" + name));
            stage.getIcons().add(icon);
        } catch (Exception e) {
            logger.error("Cannot load icon from resources:\n{}", e);
        }
    }
}
