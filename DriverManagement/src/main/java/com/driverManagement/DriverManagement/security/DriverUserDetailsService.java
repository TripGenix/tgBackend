package com.driverManagement.DriverManagement.security;

import com.driverManagement.DriverManagement.models.Driver;
import com.driverManagement.DriverManagement.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DriverUserDetailsService implements UserDetailsService {

    @Autowired
    private DriverRepository driverRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Driver driver = driverRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Driver not found with email: " + email));

        return org.springframework.security.core.userdetails.User
                .withUsername(driver.getEmail())
                .password(driver.getPassword())
                .authorities("DRIVER")
                .disabled(!driver.getEmailVerified() || driver.getIsDelete())
                .build();
    }
}

