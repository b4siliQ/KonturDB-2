package io.b4siliq.presentation.controllers;

import io.b4siliq.application.dtos.NewComponentDto;
import io.b4siliq.application.validators.ComponentDtoValidator;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public final class KonturComponentPopupController {
    @FXML private TextField nameTextField;
    @FXML private TextField specificationTextField;

    private Stage currentStage;
    private NewComponentDto resultDto;

    public void setStage(Stage stage) {
        this.currentStage = stage;
    }

    public NewComponentDto getResult() {
        return this.resultDto;
    }

    private void closePopup() {
        if (this.currentStage != null) this.currentStage.close();
    }
    @FXML
    private void addAction() {
        String name = this.nameTextField.getText();
        String spec = this.specificationTextField.getText();

        var dto = new NewComponentDto(name, spec);
        try {
            ComponentDtoValidator.validate(dto);
            this.resultDto = dto;
            this.closePopup();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void cancelAction() {
        this.resultDto = null;
        this.closePopup();
    }
}
