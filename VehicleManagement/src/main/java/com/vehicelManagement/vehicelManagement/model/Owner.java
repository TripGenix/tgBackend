package com.vehicelManagement.vehicelManagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehicle_owners")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "owner_id")
    private Integer ownerId;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "nic", length = 20)
    private String nic;

    @Column(name = "phone", length = 10)
    private String phone;

    @Column(name = "`addressLine1`", length = 50)
    private String addressLine1;

    @Column(name = "`addressLine2`", length = 50)
    private String addressLine2;

    @Column(name = "`state_province`", length = 45)
    private String stateProvince;

    @Column(name = "`postalCode`", length = 45)
    private String postalCode;

    @Column(name = "`date_of_birth`")
    private LocalDate dateOfBirth;

    @Column(name = "`owner_image`", length = 250)
    private String ownerImage;

    @Column(name = "is_delete")
    private Integer isDelete;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

