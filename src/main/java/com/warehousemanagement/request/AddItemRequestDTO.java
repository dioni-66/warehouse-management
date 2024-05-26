package com.warehousemanagement.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class AddItemRequestDTO {

    @NotEmpty
    private String name;

    @NotNull
    private Integer quantity;

    @NotNull
    private BigDecimal price;

}
