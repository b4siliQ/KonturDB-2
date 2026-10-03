package io.b4siliq.presentation.utils;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public final class FxmlOrganizerUtil {
    private FxmlOrganizerUtil() {
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
}
