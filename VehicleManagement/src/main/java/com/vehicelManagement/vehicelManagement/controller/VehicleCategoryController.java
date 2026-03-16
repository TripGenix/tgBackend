package com.vehicelManagement.vehicelManagement.controller;

import com.vehicelManagement.vehicelManagement.dto.VehicleCategoryDto;
import com.vehicelManagement.vehicelManagement.dto.WebVehicleResponse;
import com.vehicelManagement.vehicelManagement.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/save")
    public VehicleCategoryDto saveCategory(@RequestBody VehicleCategoryDto dto) {
        return categoryService.saveCategory(dto);
    }
}
