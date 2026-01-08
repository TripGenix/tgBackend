package com.example.tour_guide.dto.req;




public class TourGuideDTO {
    private String language;
    private int reviewId;
    private String image;
    private String name;
    private boolean status;
    private String nic;
    private boolean driver;

    public TourGuideDTO(String language, int reviewId, String image, String name, boolean status, String nic, boolean driver) {
        this.language = language;
        this.reviewId = reviewId;
        this.image = image;
        this.name = name;
        this.status = status;
        this.nic = nic;
        this.driver = driver;
    }

    public TourGuideDTO() {
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



