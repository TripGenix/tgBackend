package com.tripgenix.hotelmanagement.controller;

import com.tripgenix.hotelmanagement.dto.HotelDto;
import com.tripgenix.hotelmanagement.dto.ReviewDto;
import com.tripgenix.hotelmanagement.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hotels")
@CrossOrigin("*")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @PostMapping("/save")
    public HotelDto saveHotel(@RequestBody HotelDto hotelDto) {
        return hotelService.saveHotel(hotelDto);
    }

    @GetMapping("/getall")
    public List<HotelDto> getAllHotels() {
        return hotelService.getAllHotels();
    }

    @GetMapping("/{id}")
    public HotelDto getOne(@PathVariable Integer id) {
        return hotelService.getHotelById(id);
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        return hotelService.deleteHotel(id);
    }

    @PostMapping("/{id}/add-review")
    public HotelDto addReview(@PathVariable Integer id, @RequestBody ReviewDto reviewDto) {
        return hotelService.addReview(id, reviewDto);
    }
    @PutMapping("/update/{id}")
    public HotelDto updateHotel(@PathVariable Integer id, @RequestBody HotelDto hotelDto) {
        return hotelService.updateHotel(id, hotelDto);
    }
}