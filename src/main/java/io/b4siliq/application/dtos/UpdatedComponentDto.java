package io.b4siliq.application.dtos;

public record UpdatedComponentDto(
    String id,
    String name,
    String spec,
    double price,
    int quantity,
    String box,
    String desc,
    String datasheet,
    String thumbnail
) {

}
