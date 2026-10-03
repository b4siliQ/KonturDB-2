package io.b4siliq.infrastructure.database.enums;

public enum SearchColumnEnum {
    Name("name"),
    Specification("specification"),
    Box("box"),
    Favorite("favorite");

    private String column;
    private SearchColumnEnum(String column) {
        this.column = column;
    }

    public SearchColumnEnum next() {
        var values = values();
        return values[(this.ordinal() + 1) % values.length];
    }

    @Override
    public String toString() {
        return this.column;
    }
}
