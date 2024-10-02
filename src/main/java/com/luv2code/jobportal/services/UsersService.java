package com.luv2code.jobportal.services;

import com.luv2code.jobportal.entity.JobSeekerProfile;
import com.luv2code.jobportal.entity.RecruiterProfile;
import com.luv2code.jobportal.entity.Users;
import com.luv2code.jobportal.repository.JobSeekerRepository;
import com.luv2code.jobportal.repository.RecruiterProfileRepository;
import com.luv2code.jobportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UsersService {

    private final UserRepository userRepository;
    private final JobSeekerRepository jobSeekerRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsersService(UserRepository userRepository,
                        JobSeekerRepository jobSeekerRepository,
                        RecruiterProfileRepository recruiterProfileRepository,
                        PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jobSeekerRepository=jobSeekerRepository;
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Users addNew(Users users){
        users.setActive(true);
        users.setRegistrationDate(new Date(System.currentTimeMillis()));
        System.out.println("password: "+users.getPassword());
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        System.out.println("I a at this point");
        Users savedUser = userRepository.save(users);
        int userTypeId = users.getUserType().getUserTypeId();
        if(userTypeId==1){
            recruiterProfileRepository.save(new RecruiterProfile(savedUser));
        }else{
            jobSeekerRepository.save(new JobSeekerProfile(savedUser));
        }
        return savedUser;
    }

    public Optional<Users> getUserByEmailId(String email){
        return userRepository.findByEmail(email);
    }

    public Object getCurrentUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String username = authentication.getName();
            Users users = userRepository.findByEmail(username).orElseThrow(()-> new
                    UsernameNotFoundException("Could not found"+
                    "the user"));
            int userId = users.getUserId();
            if(authentication.getAuthorities().contains(new SimpleGrantedAuthority
                    ("Recruiter"))){
                    RecruiterProfile recruiterProfile = recruiterProfileRepository.findById(userId)
                            .orElse(new RecruiterProfile());
                    return recruiterProfile;
            }else{
                JobSeekerProfile jobSeekerProfile = jobSeekerRepository.findById(userId)
                        .orElse(new JobSeekerProfile());
                return jobSeekerProfile;
                
            }
        }
        return null;
    }

    public Users getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof  AnonymousAuthenticationToken)){
            String username = authentication.getName();
            Users user = userRepository.findByEmail(username).orElseThrow(()-> new
                    UsernameNotFoundException("Could not found"+
                    "the user"));
            return user;
        }
        return  null;
    }

    public Users findByEmail(String currentUsername) {
        return userRepository.findByEmail(currentUsername).orElseThrow(()->
                new UsernameNotFoundException("User not found"));
    }
}
