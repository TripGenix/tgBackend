package com.example.tour_guide.model;

import jakarta.persistence.*;


import java.util.List;

@Entity
@Table(name = "tour-guide-details")


public class TourGuide {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    @Column(name = "tour_guide_id", nullable = false, unique = true)
    private Long tourGuideId;

    @Column(name = "language", nullable = false)
    private String language;

    @Column(name = "review_id")
    private int reviewId;

    @Column(name = "image")
    private String image;

    @Column (name = "tourGuide_name", nullable = false)
    private String name;

    @Column(name = "status", nullable = false)
    private boolean status;

    @Column(name = "nic", nullable = false, unique = true)
    private String nic;

    @Column(name = "driver", nullable = false)
    private boolean driver;

    public TourGuide(String language, int reviewId, String image, String name, boolean status, String nic, boolean driver) {
        this.language = language;
        this.reviewId = reviewId;
        this.image = image;
        this.name = name;
        this.status = status;
        this.nic = nic;
        this.driver = driver;
    }

    public TourGuide() {
    }

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

    public boolean isDriver() {
        return driver;
    }

    public void setDriver(boolean driver) {
        this.driver = driver;
    }
}
