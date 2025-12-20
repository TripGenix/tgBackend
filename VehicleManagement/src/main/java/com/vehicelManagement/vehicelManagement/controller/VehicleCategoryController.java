package com.vehicelManagement.vehicelManagement.controller;

import com.vehicelManagement.vehicelManagement.dto.VehicleCategoryDto;
import com.vehicelManagement.vehicelManagement.dto.WebVehicleResponse;
import com.vehicelManagement.vehicelManagement.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/categoryController/api/v1")

public class VehicleCategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("")
    public List<VehicleCategoryDto> getVehicles() {
        return categoryService.getAllCategories();
    }
}
