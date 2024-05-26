package com.warehousemanagement.response;

import com.warehousemanagement.pojo.ApplicationUserDetails;
import lombok.Data;

@Data
public class LoadUserResponseDTO {

    private ApplicationUserDetails applicationUserDetails;

    public LoadUserResponseDTO(ApplicationUserDetails applicationUserDetails) {
        this.applicationUserDetails = applicationUserDetails;
    }
}
