package com.example.tour_guide.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tour_guide_details")
public class TourGuide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tour_guide_id")
    private Long tourGuideId;

    @Column(name = "tour_guide_name", nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String nic;

    @Column(length = 1000)
    private String description;

    @Column(name = "price_per_day")
    private Double pricePerDay;

    @Column(name = "contact_number")
    private String contactNumber;

    private String image;

    @Column(name = "experience_years")
    private Integer experienceYears;

    // You can store multiple languages as comma-separated values
    private String languages;

    public TourGuide() {
    }

    // Getters & Setters

    public Long getTourGuideId() {
        return tourGuideId;
    }

    public void setTourGuideId(Long tourGuideId) {
        this.tourGuideId = tourGuideId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(Double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }
}