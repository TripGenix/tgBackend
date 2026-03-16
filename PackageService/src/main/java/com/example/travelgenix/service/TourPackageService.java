package com.example.travelgenix.service;

import com.example.travelgenix.DTO.TourPackageDto;
import com.example.travelgenix.model.TourPackage;
import com.example.travelgenix.repository.TourPackageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TourPackageService {

    @Autowired
    private TourPackageRepository repository;

    @Autowired
    private ModelMapper mapper;

    //  SAVE PACKAGE
    public TourPackageDto save(TourPackageDto dto) {



        double totalPrice = calculateTotal(dto.getPrice(), dto.getPassengers());
        dto.setTotalPrice(totalPrice);

        TourPackage entity = mapper.map(dto, TourPackage.class);

        TourPackage saved = repository.save(entity);
        return mapper.map(saved, TourPackageDto.class);
    }

    //  UPDATE PACKAGE
    public TourPackageDto update(Long id, TourPackageDto dto) {

        TourPackage pkg = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Package not found"));

        double totalPrice = calculateTotal(dto.getPrice(), dto.getPassengers());
        dto.setTotalPrice(totalPrice);

        mapper.map(dto, pkg);
        pkg.setId(id);
        TourPackage updated = repository.save(pkg);
        return mapper.map(updated, TourPackageDto.class);
    }

    //  DELETE PACKAGE
    public void delete(Long id) {
        repository.deleteById(id);
    }

    // GET ALL
    public List<TourPackageDto> getAll() {
        return repository.findAll()
                .stream()
                .map(pkg -> mapper.map(pkg, TourPackageDto.class))
                .collect(Collectors.toList());
    }

    //  GET BY ID
    public TourPackageDto getById(Long id) {
        TourPackage pkg = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Package not found"));
        return mapper.map(pkg, TourPackageDto.class);
    }


    private double calculateTotal(double pricePerPerson, int passengers) {
        if (passengers <= 1) {
            return pricePerPerson;
        }
        // 25% discount for more than one passenger
        return pricePerPerson * passengers * 0.75;
    }

}
