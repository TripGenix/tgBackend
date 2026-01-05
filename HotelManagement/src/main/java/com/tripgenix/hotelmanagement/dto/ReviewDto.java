package com.tripgenix.hotelmanagement.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReviewDto {
    private String reviewerName;
    private double rate;
    private String comment;
    private String reviewerImage;
    private LocalDateTime date;
}