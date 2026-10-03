package io.b4siliq.presentation.controllers;

import io.b4siliq.application.dtos.UpdatedComponentDto;
import io.b4siliq.application.validators.UpdatedComponentDtoValidator;
import io.b4siliq.presentation.models.ComponentModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public final class KonturComponentUpdatePopupController {
    @FXML private TextField priceTextField;
    @FXML private TextField quantityTextField;
    @FXML private TextField boxTextField;
    @FXML private TextField datasheetTextField;
    @FXML private TextField nameTextField;
    @FXML private TextField specificationTextField;
    @FXML private TextArea descriptionTextField;
    @FXML private TextField thumbnailTextField;
    @FXML private Button findThumbnailButton;

    private Stage currentStage;
    private String currentComponentId;
    private UpdatedComponentDto resultComponent;

    public void setStage(Stage stage) {
        this.currentStage = stage;
    }

    public void initPopup(ComponentModel component) {
        this.currentComponentId = component.getId();
        this.nameTextField.setText(component.getName());
        this.specificationTextField.setText(component.getSpecification());
        this.priceTextField.setText(String.valueOf(component.getPrice()));
        this.quantityTextField.setText(String.valueOf(component.getQuantity()));
        this.boxTextField.setText(component.getBox());
        this.datasheetTextField.setText(component.getDatasheet());
        this.descriptionTextField.setText(component.getDescription());
    }

    public UpdatedComponentDto getComponent() {
        return this.resultComponent;
    }

    private void closePopup() {
        if (this.currentStage != null) {
            this.currentStage.close();
        }
    }

    @FXML
    private void addAction() {
        var name = this.nameTextField.getText();
        var spec = this.specificationTextField.getText();
        var price = Double.parseDouble(this.priceTextField.getText());
        var quantity = Integer.parseInt(this.quantityTextField.getText());
        var box = this.boxTextField.getText();
        var datasheet = this.datasheetTextField.getText();
        var desc = this.descriptionTextField.getText();
        var thumbnail = this.thumbnailTextField.getText();

        var component = new UpdatedComponentDto(
            this.currentComponentId,
            name,
            spec,
            price,
            quantity,
            box,
            desc,
            datasheet,
            thumbnail
        );
        try {
            UpdatedComponentDtoValidator.validate(component);
            this.resultComponent = component;
            this.closePopup();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void cancelAction() {
        this.resultComponent = null;
        this.closePopup();
    }
}
