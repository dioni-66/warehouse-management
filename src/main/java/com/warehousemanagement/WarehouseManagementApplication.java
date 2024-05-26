package com.warehousemanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WarehouseManagementApplication {

    /** SECURITY */
    private static final String SECURITY = "security";

    public static void main(String[] args) {
        final SpringApplication springApp = new SpringApplication(WarehouseManagementApplication.class);
        // always have security profile activated
        springApp.setAdditionalProfiles(WarehouseManagementApplication.SECURITY);
        springApp.run(args);
    }

}
