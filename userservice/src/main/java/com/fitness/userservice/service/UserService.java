package com.fitness.userservice.service;

import com.fitness.userservice.dto.RegisterRequest;
import com.fitness.userservice.dto.UserResponse;
import com.fitness.userservice.model.User;
import com.fitness.userservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;
    public UserResponse getUserProfile(String userId) {
        User user=userRepository.findById(userId)
                .orElseThrow(()->new RuntimeException("User Not Found"));

        UserResponse userResponse=new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setEmail(user.getEmail());
        userResponse.setPassword(user.getPassword());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setUpdatedAt(user.getUpdatedAt());

        return userResponse;

    }

    public UserResponse register(RegisterRequest request) {

        if(userRepository.existsByEmail(request.getEmail()))
        {
            User existingUser=userRepository.findByEmail(request.getEmail());
            UserResponse userResponse=new UserResponse();
            userResponse.setId(existingUser.getId());
            userResponse.setKeycloakId(request.getKeycloakId());
            userResponse.setEmail(existingUser.getEmail());
            userResponse.setPassword(existingUser.getPassword());
            userResponse.setFirstName(existingUser.getFirstName());
            userResponse.setLastName(existingUser.getLastName());
            userResponse.setCreatedAt(existingUser.getCreatedAt());
            userResponse.setUpdatedAt(existingUser.getUpdatedAt());
            return userResponse;
        }
        User user=new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setKeycloakId(request.getKeycloakId());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

       User saveduser= userRepository.save(user);

       UserResponse userResponse=new UserResponse();
       userResponse.setId(saveduser.getId());
        userResponse.setKeycloakId(saveduser.getKeycloakId());
       userResponse.setEmail(saveduser.getEmail());
       userResponse.setPassword(saveduser.getPassword());
       userResponse.setFirstName(saveduser.getFirstName());
       userResponse.setLastName(saveduser.getLastName());
       userResponse.setCreatedAt(saveduser.getCreatedAt());
       userResponse.setUpdatedAt(saveduser.getUpdatedAt());
       return userResponse;

    }

    public Boolean existsByUserId(String userId) {
        log.info("Calling User Validation API for userId: {}",userId);
        return userRepository.existsByKeycloakId(userId);
    }
}
