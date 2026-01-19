package com.example.travelgenix.userservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="users")
@Data

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long userId;

    private String firstName;
    private String lastName;
    private String nic;
    private LocalDate dob;
    private String email;
    private String password;
    private boolean active=true;
    private String role;
    private String phone;

    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;

}
