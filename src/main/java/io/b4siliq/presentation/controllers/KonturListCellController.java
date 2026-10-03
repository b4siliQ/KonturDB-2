package io.b4siliq.presentation.controllers;

import java.util.function.Consumer;

import io.b4siliq.presentation.models.ComponentModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public final class KonturListCellController {
    @FXML private VBox rootHBox;
    @FXML private Label nameLabel;
    @FXML private Label specificationLabel;

    private ComponentModel component;
    private Consumer<ComponentModel> deleteHandler;

    public VBox getRoot() {
        return this.rootHBox;
    }

    public void setData(ComponentModel component, Consumer<ComponentModel> handler) {
        this.component = component;
        this.deleteHandler = handler;
        this.nameLabel.textProperty().bind(component.nameProperty());
        this.specificationLabel.textProperty().bind(component.specificationProperty());
    }

    @FXML
    private void deleteAction() {
        if (this.component != null && this.deleteHandler != null) {
            this.deleteHandler.accept(this.component);
        }
    }
}
