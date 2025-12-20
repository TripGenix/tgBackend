package com.driverManagement.DriverManagement.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "drivers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "driver_id")
    private int driverId;

    @Column(name = "first_name", length = 100)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "phone1", length = 45)
    private String phone1;

    @Column(name = "phone2", length = 45)
    private String phone2;

    @Column(name = "address_line_1", length = 150)
    private String addressLine1;

    @Column(name = "address_line_2", length = 150)
    private String addressLine2;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "state_province", length = 100)
    private String stateProvince;

    @Column(name = "postal_code", length = 100)
    private String postalCode;

    @Column(name = "licen_pdf_url", length = 500)
    private String licensePdfUrl;

    @Column(name = "is_approved")
    private int isApproved;  // tinyint → int or boolean

    @Column(name = "status")
    private String status;   // if tinyint, change to int

    @Column(name = "driver_image", length = 500)
    private String driverImage;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_delete")
    private Boolean isDelete = false;

    @Column(name = "nic_number")
    private String nicNumber;
}
