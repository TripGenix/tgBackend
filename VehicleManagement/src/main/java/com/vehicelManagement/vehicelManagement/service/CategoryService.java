package com.vehicelManagement.vehicelManagement.service;

import com.vehicelManagement.vehicelManagement.dto.VehicleCategoryDto;
import com.vehicelManagement.vehicelManagement.dto.VehicleDto;
import com.vehicelManagement.vehicelManagement.model.Vehicle;
import com.vehicelManagement.vehicelManagement.model.VehicleCategory;
import com.vehicelManagement.vehicelManagement.repo.VehicleCategoryRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private VehicleCategoryRepository vehicleCategoryRepository;

    //get all
    public List<VehicleCategoryDto> getAllCategories() {
        List<VehicleCategory> categories = vehicleCategoryRepository.findAll();
        return modelMapper.map(categories, new TypeToken<List<VehicleCategoryDto>>() {}.getType());
    }
}
