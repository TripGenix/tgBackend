package com.tripgenix.hotelmanagement.service;

import com.tripgenix.hotelmanagement.dto.AmenityDto;
import com.tripgenix.hotelmanagement.dto.HotelDto;
import com.tripgenix.hotelmanagement.dto.ReviewDto;
import com.tripgenix.hotelmanagement.model.Hotel;
import com.tripgenix.hotelmanagement.model.HotelAmenity;
import com.tripgenix.hotelmanagement.model.HotelReview;
import com.tripgenix.hotelmanagement.repo.HotelRepository;
import com.tripgenix.hotelmanagement.repo.HotelReviewRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelService {

    @Autowired private HotelRepository hotelRepository;
    @Autowired private ModelMapper modelMapper;
    @Autowired
    private HotelReviewRepository reviewRepository;

    @Transactional
    public HotelDto saveHotel(HotelDto dto) {
        Hotel hotel = modelMapper.map(dto, Hotel.class);

        // Fix Relationships: Set Parent ID for Children
        if (hotel.getAmenities() != null) {
            for (HotelAmenity amenity : hotel.getAmenities()) {
                amenity.setHotel(hotel);
            }
        }
        if (hotel.getReviews() != null) {
            for (HotelReview review : hotel.getReviews()) {
                review.setHotel(hotel);
            }
        }

        Hotel savedHotel = hotelRepository.save(hotel);
        return convertToDto(savedHotel);
    }

    public List<HotelDto> getAllHotels() {
        return hotelRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public HotelDto getHotelById(Integer id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel Not Found"));
        return convertToDto(hotel);
    }

    public String deleteHotel(Integer id) {
        hotelRepository.deleteById(id);
        return "Deleted successfully";
    }

    private HotelDto convertToDto(Hotel hotel) {
        HotelDto dto = modelMapper.map(hotel, HotelDto.class);
        dto.setId(String.valueOf(hotel.getId()));
        return dto;
    }
    @Transactional
    public HotelDto addReview(Integer hotelId, ReviewDto reviewDto) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));

        HotelReview review = modelMapper.map(reviewDto, HotelReview.class);

        review.setHotel(hotel);

        hotel.getReviews().add(review);

        hotel.setReviewsCount(hotel.getReviews().size());
        double newAverage = hotel.getReviews().stream()
                .mapToDouble(HotelReview::getRate)
                .average()
                .orElse(0.0);
        hotel.setRating(newAverage);

        hotelRepository.save(hotel);

        return convertToDto(hotel);
    }
    @Transactional
    public HotelDto updateHotel(Integer id, HotelDto dto) {
        Hotel existingHotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));

        existingHotel.setName(dto.getName());
        existingHotel.setLocation(dto.getLocation());
        existingHotel.setDescription(dto.getDescription());
        existingHotel.setPricePerNight(dto.getPricePerNight());
        existingHotel.setImage(dto.getImage());
        existingHotel.setIsFavorite(dto.isFavorite());

        existingHotel.setLatitude(dto.getLatitude());
        existingHotel.setLongitude(dto.getLongitude());

        if (dto.getGallery() != null) {
            existingHotel.setGallery(dto.getGallery());
        }

        if (dto.getAmenities() != null) {
            existingHotel.getAmenities().clear();

            for (AmenityDto amenityDto : dto.getAmenities()) {
                HotelAmenity amenity = modelMapper.map(amenityDto, HotelAmenity.class);
                amenity.setHotel(existingHotel);
                existingHotel.getAmenities().add(amenity);
            }
        }

        Hotel updatedHotel = hotelRepository.save(existingHotel);
        return convertToDto(updatedHotel);
    }}