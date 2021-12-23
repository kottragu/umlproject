package com.kottragu.umlproject.service;

import com.kottragu.umlproject.model.User;
import com.kottragu.umlproject.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    public void saveUser(User user) {
        if (userRepository.findUserByUsername(user.getUsername()) == null) {
            userRepository.save(user);
        }
    }

    public void updateUser(User user) {
        if (userRepository.findById(user.getId()).isPresent()) {
            userRepository.updateUser(user.getId(), user.getUsername(), user.getPassword(), user.getRole());
        }
    }

    public User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

}