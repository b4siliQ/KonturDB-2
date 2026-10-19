package io.b4siliq.presentation.models;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.b4siliq.application.contracts.Popup;
import io.b4siliq.presentation.utils.FXOrganizerUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PopupModel implements Popup {
    private static final Logger logger = LoggerFactory.getLogger(PopupModel.class);

    private Stage popup;
    private Scene popupScene;

    public PopupModel(FXMLLoader loader) {
        try {
            this.popup = new Stage();
            this.popupScene = new Scene(loader.load());
            this.popup.setScene(this.popupScene);
        } catch (IOException e) {
            logger.error("An error has occured while initializing popup", e);
            throw new RuntimeException("Failed to initialize popup", e);
        }
    }

    public PopupModel(String name, boolean resizable, FXMLLoader loader) {
        this(name, resizable, "KDB2Icon.png", loader);
    }

    public PopupModel(String name, boolean resizable, String iconName, FXMLLoader loader) {
        this(loader);
        this.popup.setTitle(name);
        this.popup.setResizable(resizable);
        this.popup.initModality(Modality.APPLICATION_MODAL);
        FXOrganizerUtil.setIcon(this.popup, iconName);
    }

    public Stage getPopupStage() {
        return this.popup;
    }

    public Scene getPopupScene() {
        return this.popupScene;
    }

    @Override
    public void showPopup() {
        this.popup.showAndWait();
    }
}
