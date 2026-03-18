package com.example.tour_guide.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tour_guide_details")
public class TourGuide {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "tour_guide_id")
    private Long tourGuideId;

    private String language;
    private int reviewId;
    private String image;

    @Column(name = "tour_guide_name")
    private String name;

    private boolean status;
    private String nic;
    private Integer driver;
    private Double hourlyRate;

    public TourGuide() {
    }

    // getters & setters

    public Long getTourGuideId() {
        return tourGuideId;
    }

    public void setTourGuideId(Long tourGuideId) {
        this.tourGuideId = tourGuideId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public Integer getDriver() {
        return driver;
    }

    public void setDriver(Integer driver) {
        this.driver = driver;
    }

    public Double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(Double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }
}
