package io.b4siliq.presentation.controllers;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.b4siliq.application.services.ComponentService;
import io.b4siliq.infrastructure.database.enums.SearchColumnEnum;
import io.b4siliq.presentation.models.ComponentModel;
import io.b4siliq.presentation.models.ListCellModel;
import io.b4siliq.presentation.utils.FontLoaderUtil;
import io.b4siliq.presentation.utils.FxmlOrganizerUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;

public final class KonturMainController {
    @FXML private Label searchParameterLabel;
    @FXML private TextField searchTextField;
    @FXML private ListView<ComponentModel> componentListView;
    @FXML private Button searchConfigButton;
    @FXML private Button addNewComponentButton;
    @FXML private Button searchButton;
    @FXML private Button updateComponentButton;
    @FXML private Button setFavoriteButton;
    @FXML private Button datasheetButton;
    @FXML private Label searchLogoLabel;
    @FXML private Label nameLabel;
    @FXML private Label specificationLabel;
    @FXML private Label priceLabel;
    @FXML private Label quantityLabel;
    @FXML private Label boxLabel;
    @FXML private Label staticPriceLabel;
    @FXML private Label staticQuantityLabel;
    @FXML private Label staticBoxLabel;
    @FXML private Label staticDescriptionLabel;
    @FXML private TextFlow descriptionTextFlow;
    @FXML private ScrollPane browserScrollPane;

    private static final Logger logger = LoggerFactory.getLogger(KonturMainController.class);
    private final Text descriptionText = new Text();
    private ComponentService service;
    private SearchColumnEnum column;

    public void preapareController(ComponentService service) {
        this.service = service;
        this.column = SearchColumnEnum.Name;

        this.componentListView.setCellFactory(param -> new ListCellModel(item -> {
            this.service.deleteComponent(UUID.fromString(item.getId())).thenRun(() -> {
                logger.info("Deleted component from data base");
                Platform.runLater(() -> {
                    this.componentListView.getItems().remove(item);
                });
            });
            this.componentListView.getItems().remove(item);
        }));

        this.service.initializeDB().thenRun(() -> {
            logger.info("Creating table...");
            this.refreshComponentList();
        });
    }

    private void refreshComponentList() {
        this.service.getAllComponents()
            .thenAccept(components -> {
                logger.info("Refreshing...");
                Platform.runLater(() -> {
                    var models = components.stream()
                        .map(trans -> new ComponentModel(
                            trans.id,
                            trans.name,
                            trans.specification,
                            trans.price,
                            trans.quantity,
                            trans.box,
                            trans.description,
                            trans.datasheet,
                            trans.thumbnail,
                            trans.favorite
                            )
                        )
                        .toList();
                    this.componentListView.getItems().setAll(models);
                });
            })
            .exceptionally(ex -> {
                Platform.runLater(() -> {
                    logger.error(
                        "An error has occured while updating component list:\n{}",
                        ex.getMessage()
                    );
                });
                return null;
            });
    }
    @FXML
    private void initialize() {
        var emojiFont = FontLoaderUtil.loadFont(KonturMainController.class, "NotoEmoji.ttf", 13);
        var headFont = FontLoaderUtil.loadFont(KonturMainController.class, "ScienceGothic.ttf", 48);
        var handwriteFont = FontLoaderUtil.loadFont(KonturMainController.class, "LoraItalic.ttf", 24);
        var browserFont = FontLoaderUtil.loadFont(KonturMainController.class, "IBMPlexSerif.ttf", 18);
        var elementFont = FontLoaderUtil.loadFont(KonturMainController.class, "FiraSans.ttf", 13);

        this.nameLabel.setFont(headFont);
        this.specificationLabel.setFont(handwriteFont);
        this.priceLabel.setFont(browserFont);
        this.quantityLabel.setFont(browserFont);
        this.boxLabel.setFont(browserFont);
        this.descriptionText.setFont(browserFont);

        this.staticPriceLabel.setFont(browserFont);
        this.staticQuantityLabel.setFont(browserFont);
        this.staticBoxLabel.setFont(browserFont);
        this.staticDescriptionLabel.setFont(browserFont);

        this.searchParameterLabel.setFont(elementFont);
        this.datasheetButton.setFont(elementFont);
        this.searchTextField.setFont(elementFont);

        this.searchLogoLabel.setFont(emojiFont);
        this.searchConfigButton.setFont(emojiFont);
        this.searchButton.setFont(emojiFont);
        this.addNewComponentButton.setFont(emojiFont);
        this.setFavoriteButton.setFont(emojiFont);
        this.updateComponentButton.setFont(emojiFont);

        this.descriptionTextFlow.getChildren().add(this.descriptionText);
    }

    @FXML
    private void openSearchConfigurationPopupAction() {
        switch(this.column) {
            case SearchColumnEnum.Favorite -> {
                this.searchParameterLabel.setText("Компоненты: По Имени");
                this.column = this.column.next();
            }
            case SearchColumnEnum.Name -> {
                this.searchParameterLabel.setText("Компоненты: По Типу");
                this.column = this.column.next();
            }
            case SearchColumnEnum.Specification -> {
                this.searchParameterLabel.setText("Компоненты: По Ящику");
                this.column = this.column.next();
            }
            case SearchColumnEnum.Box -> {
                this.searchParameterLabel.setText("Компоненты: Избранное");
                this.column = this.column.next();
            }
        };
    }

    @FXML
    private void addNewComponentAction() {
        try {
            var loader = FxmlOrganizerUtil.getLoader(KonturMainController.class, "KonturComponentPopup.fxml");
            Parent popupRoot = loader.load();
            KonturComponentPopupController popupController = loader.getController();

            var popupStage = new Stage();
            popupStage.setScene(new Scene(popupRoot));
            popupController.setStage(popupStage);

            popupStage.setResizable(false);
            popupStage.setTitle("Регистратор нового компонента");
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.showAndWait();

            var result = popupController.getResult();
            if (result != null) {
                this.service.createNewComponent(result).thenRun(() -> {
                    logger.info("Creating new component from dto");
                    this.refreshComponentList();
                });
            }
        } catch(IOException e) {
            logger.error(
                "An error has occured while creating new component",
                e.getMessage()
            );
            // Custom ex
        }
    }

    @FXML
    private void getComponentCellAction() {
        var currentComponent = this.componentListView.getSelectionModel().getSelectedItem();
        if (currentComponent == null) return;

        this.nameLabel.textProperty().bind(currentComponent.nameProperty());
        this.specificationLabel.textProperty().bind(currentComponent.specificationProperty());
        this.priceLabel.textProperty().bind(currentComponent.priceProperty().asString());
        this.quantityLabel.textProperty().bind(currentComponent.quantityProperty().asString());
        this.boxLabel.textProperty().bind(currentComponent.boxProperty());
        this.descriptionText.textProperty().bind(currentComponent.descriptionProperty());
        this.browserScrollPane.setVisible(true);
    }

    @FXML
    private void searchAction() {
        if (this.searchTextField.getText().isBlank()) {
            this.refreshComponentList();
        } else {
            this.service.searchService(this.column, this.searchTextField.getText())
                .thenAccept(items -> {
                    logger.info("Searching...");
                    Platform.runLater(() -> {
                        var models = items.stream()
                            .map(trans -> new ComponentModel(
                                trans.id,
                                trans.name,
                                trans.specification,
                                trans.price,
                                trans.quantity,
                                trans.box,
                                trans.description,
                                trans.datasheet,
                                trans.thumbnail,
                                trans.favorite
                                )
                            )
                            .toList();
                        this.componentListView.getItems().setAll(models);
                    });
                })
                .exceptionally(ex -> {
                    Platform.runLater(() -> {
                        logger.error(
                            "An error has occured while searching components:\\n{}",
                            ex.getMessage()
                        );
                    });

                    return null;
                });
        }
    }

    @FXML
    private void updateComponentAction() {
        var currentComponent = this.componentListView.getSelectionModel().getSelectedItem();
        if (currentComponent == null) return;

        try {
            var loader = FxmlOrganizerUtil.getLoader(
                KonturMainController.class,
                "KonturComponentUpdatePopup.fxml"
            );
            Parent popupRoot = loader.load();
            KonturComponentUpdatePopupController popupController = loader.getController();

            var popupStage = new Stage();
            popupStage.setScene(new Scene(popupRoot));
            popupController.setStage(popupStage);
            popupController.initPopup(currentComponent);

            popupStage.setResizable(false);
            popupStage.setTitle("Редактор компонента");
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.showAndWait();

            var result = popupController.getComponent();
            if (result != null) {
                this.service.updateComponent(result).thenRun(() -> {
                    logger.info("Redacting component by dto");
                    Platform.runLater(() -> {
                        currentComponent.setDataFromUpdateDto(result);
                    });
                });
            }
        } catch(IOException e) {
            logger.error(
                "An error has occured while redacting component",
                e.getMessage()
            );
        }
    }

    @FXML
    private void setFavoriteComponentAction() {
        var currentComponent = this.componentListView.getSelectionModel().getSelectedItem();
        var setToFlag = currentComponent.getFavorite();

        this.service.setComponentToFavorite(UUID.fromString(currentComponent.getId()), setToFlag);
    }

    @FXML
    private void openDatasheetAction() {
        var currentComponent = this.componentListView.getSelectionModel().getSelectedItem();
        if (currentComponent == null) return;

        var doc = new File(currentComponent.getDatasheet());
        try {
            if (doc.exists() && Desktop.isDesktopSupported()) {
                logger.info("Opening datasheet file");
                Desktop.getDesktop().open(doc);
            }
        } catch(Exception e) {
            // Soon
        }
    }
}
