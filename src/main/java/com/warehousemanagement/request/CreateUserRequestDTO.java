package com.warehousemanagement.request;

import lombok.Data;

@Data
public class CreateUserRequestDTO {
    private String username;
    private String email;
    private boolean enabled;
    private String firstname;
    private String lastname;
    private String password;
    private String role;

}
