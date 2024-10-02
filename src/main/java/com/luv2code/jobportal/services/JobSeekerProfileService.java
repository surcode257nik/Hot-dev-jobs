package com.luv2code.jobportal.services;

import com.luv2code.jobportal.entity.JobSeekerProfile;
import com.luv2code.jobportal.entity.RecruiterProfile;
import com.luv2code.jobportal.entity.Users;
import com.luv2code.jobportal.repository.JobSeekerRepository;
import com.luv2code.jobportal.repository.UserRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JobSeekerProfileService {

    private final JobSeekerRepository jobSeekerRepository;
    private final UserRepository userRepository;

    public JobSeekerProfileService(JobSeekerRepository jobSeekerRepository,
                                   UserRepository userRepository) {
        this.jobSeekerRepository = jobSeekerRepository;
        this.userRepository = userRepository;
    }

    public Optional<JobSeekerProfile> getOne(Integer id){
        return jobSeekerRepository.findById(id);
    }

    public JobSeekerProfile addNew(JobSeekerProfile jobSeekerProfile) {
        return jobSeekerRepository.save(jobSeekerProfile);
    }

    public JobSeekerProfile getCurrentSeekerProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String currentUserName = authentication.getName();
            Users users = userRepository.findByEmail(currentUserName).orElseThrow(()->
                    new UsernameNotFoundException("User not found"));

            Optional<JobSeekerProfile> jobSeekerProfile = getOne(users.getUserId());
            return jobSeekerProfile.orElse(null);
        }else
            return null;
    }

}
