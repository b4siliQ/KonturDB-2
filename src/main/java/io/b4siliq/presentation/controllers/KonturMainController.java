package io.b4siliq.presentation.controllers;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.b4siliq.application.enums.SearchComponentColumnEnum;
import io.b4siliq.application.services.ComponentService;
import io.b4siliq.presentation.models.ComponentModel;
import io.b4siliq.presentation.models.ListCellModel;
import io.b4siliq.presentation.utils.FxmlOrganizerUtil;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;

public final class KonturMainController {
    @FXML private Label searchParameterLabel;
    @FXML private TextField searchTextField;
    @FXML private ListView<ComponentModel> componentListView;
    @FXML private Label nameLabel;
    @FXML private Label specificationLabel;
    @FXML private Label priceLabel;
    @FXML private Label quantityLabel;
    @FXML private Label boxLabel;
    @FXML private TextFlow descriptionTextFlow;
    @FXML private ScrollPane browserScrollPane;
    @FXML private ImageView thumbnailImageView;

    private static final Logger logger = LoggerFactory.getLogger(KonturMainController.class);
    private final Text descriptionText = new Text();
    private ComponentService service;
    private SearchComponentColumnEnum column;
    private Image defaultThumbnail;

    public void preapareController(ComponentService service) {
        this.service = service;
        this.column = SearchComponentColumnEnum.Name;
        this.defaultThumbnail = this.loadDefaultThumbnail();

        this.componentListView.setCellFactory(param -> new ListCellModel(item -> {
            this.service.deleteComponent(UUID.fromString(item.getId())).thenRun(() -> {
                logger.info("Deleted component from data base");
                Platform.runLater(() -> {
                    this.componentListView.getItems().remove(item);
                });
            });
            this.componentListView.getItems().remove(item);
        }));

        this.componentListView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldComponent, newComponent) -> this.updateSelection(oldComponent, newComponent)
        );

        this.service.initializeDB().thenRun(() -> {
            logger.info("Initializing table...");
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

    private Image loadDefaultThumbnail() {
        var resource = KonturMainController.class.getResource("/io/b4siliq/styles/KonturDark/images/KonturNull.png");
        if (resource != null) return new Image(resource.toExternalForm());

        logger.error("An error has occured while loading default component thumbnail");
        return null;
    }

    private void updateThumbnail(String path) {
        if (path != null && !path.isBlank()) {
            var file = new File(path);
            if (file.exists()) {
                this.thumbnailImageView.setImage(new Image(file.toURI().toString()));
                return;
            }
        }
        this.thumbnailImageView.setImage(defaultThumbnail);
    }

    private void onThumbnailPathChanged(ObservableValue<? extends String> obs, String oldPath, String newPath) {
        this.updateThumbnail(newPath);
    }

    private void bindElements(ComponentModel component) {
        this.nameLabel.textProperty().bind(component.nameProperty());
        this.specificationLabel.textProperty().bind(component.specificationProperty());
        this.priceLabel.textProperty().bind(component.priceProperty().asString());
        this.quantityLabel.textProperty().bind(component.quantityProperty().asString());
        this.boxLabel.textProperty().bind(component.boxProperty());
        this.descriptionText.textProperty().bind(component.descriptionProperty());

        component.thumbnailProperty().addListener(this::onThumbnailPathChanged);
    }

    private void unbindElements(ComponentModel component) {
        this.nameLabel.textProperty().unbind();
        this.specificationLabel.textProperty().unbind();
        this.priceLabel.textProperty().unbind();
        this.quantityLabel.textProperty().unbind();
        this.boxLabel.textProperty().unbind();
        this.descriptionText.textProperty().unbind();

        component.thumbnailProperty().removeListener(this::onThumbnailPathChanged);
    }

    private void updateSelection(ComponentModel oldComponent, ComponentModel newComponent) {
        if (oldComponent != null) {
            this.unbindElements(oldComponent);
        }

        if (newComponent == null) {
            this.browserScrollPane.setVisible(false);
            return;
        }

        this.bindElements(newComponent);
        updateThumbnail(newComponent.getThumbnail());
        this.browserScrollPane.setVisible(true);
    }
    @FXML
    private void initialize() {
        this.descriptionText.getStyleClass().add("text-for-flow-classic");
        this.descriptionTextFlow.getChildren().add(this.descriptionText);
    }

    @FXML
    private void openSearchConfigurationPopupAction() {
        switch(this.column) {
            case SearchComponentColumnEnum.Favorite -> {
                this.searchParameterLabel.setText("Компоненты: По Имени");
                this.column = this.column.next();
            }
            case SearchComponentColumnEnum.Name -> {
                this.searchParameterLabel.setText("Компоненты: По Типу");
                this.column = this.column.next();
            }
            case SearchComponentColumnEnum.Specification -> {
                this.searchParameterLabel.setText("Компоненты: По Ящику");
                this.column = this.column.next();
            }
            case SearchComponentColumnEnum.Box -> {
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
        if (currentComponent == null || currentComponent.getDatasheet() == null) return;

        var doc = new File(currentComponent.getDatasheet());

        if (!doc.exists()) {
            logger.error(
                "An error has occured while opening datasheet:\nCannot find this file {}",
                currentComponent.getDatasheet()
            );
            return;
        }

        if (!Desktop.isDesktopSupported()) {
            logger.error(
                "An error has occured while opening datasheet:\nThis desktop isn't supported"
            );
            return;
        }

        CompletableFuture.runAsync(() -> {
            try {
                logger.info("Opening datasheet file");
                Desktop.getDesktop().open(doc);
            } catch(Exception e) {
                logger.error(
                    "An error has occured while opening datasheet:\nCannot open this file {}",
                    currentComponent.getDatasheet()
                );
            }
        });
    }
}
