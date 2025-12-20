package com.vehicelManagement.vehicelManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OwnerDto {
    private Long id;
    private String name;
    private String nic;
    private String phone;
    private String addressLine1;
    private String addressLine2;
    private String stateProvince;
    private String postalCode;
    private String dateOfBirth;
    private String ownerImage;
}
