package com.tripgenix.AuthService.services;

import com.tripgenix.AuthService.dto.TouristDto;
import com.tripgenix.AuthService.dto.TouristResponseDto;
import com.tripgenix.AuthService.model.Tourist;
import com.tripgenix.AuthService.repo.TouristRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class TouristService implements UserDetailsService {

    private final TouristRepository repository;
    private final ModelMapper mapper;
    private final PasswordEncoder encoder;

    public TouristService(TouristRepository repository, ModelMapper mapper, PasswordEncoder encoder) {
        this.repository = repository;
        this.mapper = mapper;
        this.encoder = encoder;
    }

    public TouristResponseDto saveTourist(TouristDto dto) {

        Tourist user = mapper.map(dto, Tourist.class);

        if (dto.getDateOfBirth() != null && !dto.getDateOfBirth().isEmpty()) {
            user.setDateOfBirth(LocalDate.parse(dto.getDateOfBirth()));
        }

        user.setPassword(encoder.encode(dto.getPassword()));
        user.setCreatedAt(LocalDateTime.now());

        Tourist saved = repository.save(user);

        return mapper.map(saved, TouristResponseDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Tourist user = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities("TOURIST")
                .build();
    }

    public Tourist findByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }
}
