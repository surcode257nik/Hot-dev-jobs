package com.luv2code.jobportal.services;

import com.luv2code.jobportal.entity.Users;
import com.luv2code.jobportal.repository.UserRepository;
import com.luv2code.jobportal.util.CustomerUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByEmail(username).orElseThrow(()->
                new UsernameNotFoundException("Could not found the user"));
        return new CustomerUserDetails(user);
    }
}
