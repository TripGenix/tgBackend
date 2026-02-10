package com.driverManagement.DriverManagement.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@Table(name = "driver_blocked_dates")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverBlockedDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "driver_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Driver driver;

    @Column(name = "blocked_date", nullable = false)
    private LocalDate blockedDate;

    // Optional: Reason for blocking (e.g., "Sick", "Personal")
    @Column(name = "reason")
    private String reason;
}