package io.b4siliq.application.enums;

import io.b4siliq.application.contracts.SearchColumnEnum;

public enum SearchComponentColumnEnum implements SearchColumnEnum {
    Name("name"),
    Specification("specification"),
    Box("box"),
    Favorite("favorite");

    private String column;
    private SearchComponentColumnEnum(String column) {
        this.column = column;
    }

    @Override
    public SearchComponentColumnEnum next() {
        var values = values();
        return values[(this.ordinal() + 1) % values.length];
    }

    @Override
    public String toString() {
        return this.column;
    }
}
