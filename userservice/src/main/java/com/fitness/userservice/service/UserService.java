package com.fitness.userservice.service;

import com.fitness.userservice.dto.RegisterRequest;
import com.fitness.userservice.dto.UserResponse;
import com.fitness.userservice.model.User;
import com.fitness.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
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
            throw new RuntimeException("Email already exists");
        }
        User user=new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
       User saveduser= userRepository.save(user);

       UserResponse userResponse=new UserResponse();
       userResponse.setId(saveduser.getId());
       userResponse.setEmail(saveduser.getEmail());
       userResponse.setPassword(saveduser.getPassword());
       userResponse.setFirstName(saveduser.getFirstName());
       userResponse.setLastName(saveduser.getLastName());
       userResponse.setCreatedAt(saveduser.getCreatedAt());
       userResponse.setUpdatedAt(saveduser.getUpdatedAt());
       return userResponse;

    }
}
