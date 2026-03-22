package com.example.tour_guide.dto.req;

public class TourGuideDTO {

    private Long tourGuideId;
    private String name;
    private String nic;
    private String description;
    private Double pricePerDay;
    private String contactNumber;
    private String image;
    private Integer experienceYears;
    private String languages;

    public TourGuideDTO() {
    }

    public TourGuideDTO(Long tourGuideId, String name, String nic,
                        String description, Double pricePerDay,
                        String contactNumber, String image,
                        Integer experienceYears, String languages) {
        this.tourGuideId = tourGuideId;
        this.name = name;
        this.nic = nic;
        this.description = description;
        this.pricePerDay = pricePerDay;
        this.contactNumber = contactNumber;
        this.image = image;
        this.experienceYears = experienceYears;
        this.languages = languages;
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