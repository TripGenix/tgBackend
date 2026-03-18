package com.example.travelgenix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// Remove the imports for ModelMapper and Bean since we aren't using them here anymore

@SpringBootApplication
public class PackageServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PackageServiceApplication.class, args);
    }

}