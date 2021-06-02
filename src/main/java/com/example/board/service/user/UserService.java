package com.example.board.service.user;

import com.example.board.common.custom_exception.UserEmailExistException;
import com.example.board.common.custom_exception.UserNicknameExistException;
import com.example.board.dto.user.UserDTO;
import com.example.board.dto.user.signup.SignupDTO;
import com.example.board.entity.User;
import com.example.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDTO signup(SignupDTO signupDTO) {
        Optional<User> byEmail = userRepository.findByEmail(signupDTO.getEmail());
        Optional<User> byName = userRepository.findByName(signupDTO.getNickname());

        byEmail.ifPresentOrElse(
                user -> {
                    throw new UserEmailExistException();
                },
                () -> {
                }
        );

        byName.ifPresentOrElse(
                user -> {
                    throw new UserNicknameExistException();
                },
                () -> {
                }
        );


        User user = User.builder()
                .email(signupDTO.getEmail())
                .name(signupDTO.getNickname())
                .build();

        user.passwordEncryption(signupDTO.getPassword());

        User savedUser = userRepository.save(user);

        return UserDTO.builder()
                .nickname(savedUser.getName())
                .build();
    }

}
