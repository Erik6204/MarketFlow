package com.example.marketflow.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.marketflow.Repository.UserRepository;
import com.example.marketflow.User.LoginRequest;
import com.example.marketflow.User.RegisterRequest;
import com.example.marketflow.User.ShowUserDto;
import com.example.marketflow.User.UserEntity;
import com.example.marketflow.User.UserMapper;
import com.example.marketflow.exception.EmailAlreadyExistsException;
import com.example.marketflow.exception.InvalidCredentialsException;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public void register (RegisterRequest userDto){

        if (userRepository.existsByEmailIgnoreCase(userDto.getEmail())) {
            throw new EmailAlreadyExistsException(userDto.getEmail());
        }

        String passwordHash =passwordEncoder.encode(userDto.getPassword());

        userRepository.save(UserMapper.convert(userDto, passwordHash));
    }

    @Transactional(readOnly = true)
    public Optional<ShowUserDto> authenticate(LoginRequest log)
    {
        Optional<UserEntity> user =
                userRepository.findByEmailIgnoreCase(log.getEmail().trim())
                .orElseThrow(InvalidCredentialsException::new);

        if (user.isEmpty()) {
            return Optional.empty();
        }

        boolean passwordCorrect = passwordEncoder.matches(
                log.getPassword(),
                user.get().getPasswordHash()
        );

        if (!passwordCorrect) {
            return Optional.empty();
        }

        return Optional.of(UserMapper.toShowUserDto(user.get()));
    }

    @Transactional(readOnly = true)
    public ShowUserDto getUserById(Long id){
        return userRepository.findById(id)
                .map(UserMapper::toShowUserDto)
                .orElse(null);
    }

    
    
}
