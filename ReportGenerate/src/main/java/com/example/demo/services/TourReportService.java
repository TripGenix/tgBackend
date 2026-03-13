package com.example.demo.services;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TourReportService {

    private final JdbcTemplate jdbcTemplate;

    public TourReportService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /* ===============================
       GET ALL TOUR REPORTS
    =============================== */

    public List<Map<String, Object>> getAllTours() {

        String sql = """
                SELECT 
                    b.reference_id AS referenceId,
                    b.booker_name AS touristName,
                    b.arrival_date_time AS startDate,
                    b.departure_date_time AS endDate,
                    CONCAT(d.first_name,' ',d.last_name) AS driverName,
                    v.vehicle_name AS vehicleName,
                    tp.name AS packageName,
                    v.booking_price AS cost
                FROM bookings b
                LEFT JOIN drivers d ON b.driver_id = d.driver_id
                LEFT JOIN vehicle v ON b.vehicle_id = v.vehicle_id
                LEFT JOIN travel_packages tp ON b.package_id = tp.id
                ORDER BY b.date_created DESC
                """;

        return jdbcTemplate.queryForList(sql);
    }


    /* ===============================
       GET TOUR REPORT BY DATE RANGE
    =============================== */

    public List<Map<String, Object>> getToursByDate(String from, String to) {

        String sql = """
                SELECT 
                    b.reference_id AS referenceId,
                    b.booker_name AS touristName,
                    b.arrival_date_time AS startDate,
                    b.departure_date_time AS endDate,
                    CONCAT(d.first_name,' ',d.last_name) AS driverName,
                    v.vehicle_name AS vehicleName,
                    tp.name AS packageName,
                    v.booking_price AS cost
                FROM bookings b
                LEFT JOIN drivers d ON b.driver_id = d.driver_id
                LEFT JOIN vehicle v ON b.vehicle_id = v.vehicle_id
                LEFT JOIN travel_packages tp ON b.package_id = tp.id
                WHERE DATE(b.date_created) BETWEEN ? AND ?
                ORDER BY b.date_created DESC
                """;

        return jdbcTemplate.queryForList(sql, from, to);
    }
}