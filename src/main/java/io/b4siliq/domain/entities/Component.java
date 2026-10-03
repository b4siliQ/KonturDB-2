package io.b4siliq.domain.entities;

public class Component{
    public String id;
    public String name;
    public String specification;
    public Double price;
    public Integer quantity;
    public String box;
    public String description;
    public String datasheet;
    public String thumbnail;
    public Integer favorite;

    public boolean isFavorite() {
        return this.favorite != null && this.favorite == 1;
    }

    public void setThumbnail(String path) {
        this.thumbnail = path;
    }
}
