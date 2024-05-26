package com.warehousemanagement.request;

import lombok.Data;

@Data
public class UpdateUserRequestDTO {
    private int userId;
    private String firstname;
    private String lastname;
    private boolean enabled;
    private String email;
}
