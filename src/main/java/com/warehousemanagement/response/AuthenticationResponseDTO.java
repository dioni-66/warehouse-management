package com.warehousemanagement.response;

import com.warehousemanagement.pojo.ApplicationUserDetails;
import lombok.Data;

@Data
public class AuthenticationResponseDTO {
    private ApplicationUserDetails applicationUserDetails;

    public AuthenticationResponseDTO(ApplicationUserDetails applicationUserDetails) {
        this.applicationUserDetails = applicationUserDetails;
    }
}
