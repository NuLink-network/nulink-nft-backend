package com.project.nulinknft.service;

import com.project.nulinknft.entity.User;
import com.project.nulinknft.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserService{

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findById(long id){
        return userRepository.findById(id).orElse(null);
    }

    public User findByAddress(String address){
        return userRepository.findByAddress(address);
    }

    @Transactional
    public void save(User user){
        userRepository.save(user);
    }
}
