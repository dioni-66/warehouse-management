package com.warehousemanagement.response;

import lombok.Data;

@Data
public class ApproveOrderResponseDTO {
    private String message;

    public ApproveOrderResponseDTO(String message) {
        this.message = message;
    }
}
