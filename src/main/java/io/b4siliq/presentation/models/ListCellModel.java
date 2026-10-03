package io.b4siliq.presentation.models;

import java.io.IOException;
import java.util.function.Consumer;

import io.b4siliq.presentation.controllers.KonturListCellController;
import io.b4siliq.presentation.utils.FxmlOrganizerUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;

public class ListCellModel extends ListCell<ComponentModel> {
    private FXMLLoader loader;
    private KonturListCellController controller;
    private final Consumer<ComponentModel> deleteHandler;

    public ListCellModel(Consumer<ComponentModel> handler) {
        this.deleteHandler = handler;
    }

    @Override
    public void updateItem(ComponentModel item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (this.loader == null) {
                try {
                    this.loader = FxmlOrganizerUtil.getLoader(ListCellModel.class, "KonturListCell.fxml");
                    this.loader.load();
                    controller = loader.getController();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }

            controller.setData(item, this.deleteHandler);
            setText(null);
            setGraphic(controller.getRoot());
        }
    }
}
