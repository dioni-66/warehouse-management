package com.warehousemanagement.response;

import com.warehousemanagement.pojo.ApplicationUserDetails;
import lombok.Data;

@Data
public class UpdateUserResponseDTO {

    private ApplicationUserDetails applicationUserDetails;

    public UpdateUserResponseDTO(ApplicationUserDetails applicationUserDetails) {
        this.applicationUserDetails = applicationUserDetails;
    }
}
