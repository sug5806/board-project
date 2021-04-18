package com.example.board.service;

import com.example.board.dto.SignupDTO;
import com.example.board.dto.UserDTO;
import com.example.board.entity.User;
import com.example.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDTO signup(SignupDTO signupDTO) {
        User user = User.builder()
                .email(signupDTO.getEmail())
                .password(signupDTO.getPassword())
                .name(signupDTO.getNickname())
                .build();


        User savedUser = userRepository.save(user);

        return UserDTO.builder()
                .nickname(savedUser.getName())
                .build();
    }

}
