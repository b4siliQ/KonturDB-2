package io.b4siliq.presentation.models;

import io.b4siliq.application.dtos.UpdatedComponentDto;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ComponentModel {
    private final String id;
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty spec = new SimpleStringProperty();
    private final DoubleProperty price = new SimpleDoubleProperty();
    private final IntegerProperty quantity = new SimpleIntegerProperty();
    private final StringProperty box = new SimpleStringProperty();
    private final StringProperty desc = new SimpleStringProperty();
    private final StringProperty datasheet = new SimpleStringProperty();
    private final StringProperty thumbnail = new SimpleStringProperty();
    private final BooleanProperty favorite = new SimpleBooleanProperty();

    public ComponentModel(
        String id,
        String name,
        String spec,
        double price,
        int quantity,
        String box,
        String desc,
        String datasheet,
        String thumbnail,
        int favorite
    ) {
        this.id = id;
        this.name.set(name);
        this.spec.set(spec);
        this.price.set(price);
        this.quantity.set(quantity);
        this.box.set(box);
        this.desc.set(desc);
        this.datasheet.set(datasheet);
        this.thumbnail.set(thumbnail);

        if (favorite == 1) this.favorite.set(true);
        else this.favorite.set(false);
    }

    public void setDataFromUpdateDto(UpdatedComponentDto dto) {
        this.setName(dto.name());
        this.setSpecification(dto.spec());
        this.setPrice(dto.price());
        this.setQuantity(dto.quantity());
        this.setBox(dto.box());
        this.setDescription(dto.desc());
        this.setDatasheet(dto.datasheet());
        this.setThumbnail(dto.thumbnail());
    }

    public String getId() { return this.id; }

    public String getName() { return this.name.get(); }
    public void setName(String name) { this.name.set(name); }
    public StringProperty nameProperty() { return this.name; }

    public String getSpecification() { return this.spec.get(); }
    public void setSpecification(String spec) { this.spec.set(spec); }
    public StringProperty specificationProperty() { return this.spec; }

    public double getPrice() { return this.price.get(); }
    public void setPrice(double price) { this.price.set(price); }
    public DoubleProperty priceProperty() { return this.price; }

    public int getQuantity() { return this.quantity.get(); }
    public void setQuantity(int quantity) { this.quantity.set(quantity); }
    public IntegerProperty quantityProperty() { return this.quantity; }

    public String getBox() { return this.box.get(); }
    public void setBox(String Box) { this.box.set(Box); }
    public StringProperty boxProperty() { return this.box; }

    public String getDescription() { return this.desc.get(); }
    public void setDescription(String desc) { this.desc.set(desc); }
    public StringProperty descriptionProperty() { return this.desc; }

    public String getDatasheet() { return this.datasheet.get(); }
    public void setDatasheet(String datasheet) { this.datasheet.set(datasheet); }
    public StringProperty datasheetProperty() { return this.datasheet; }

    public String getThumbnail() { return this.thumbnail.get(); }
    public void setThumbnail(String thumbnail) { this.thumbnail.set(thumbnail); }
    public StringProperty thumbnailProperty() { return this.thumbnail; }

    public boolean getFavorite() { return this.favorite.get(); }
    public void setFavorite(boolean favorite) { this.favorite.set(favorite); }
    public BooleanProperty favoriteProperty() { return this.favorite; }
}
