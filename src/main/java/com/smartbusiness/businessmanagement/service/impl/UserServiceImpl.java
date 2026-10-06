package com.smartbusiness.businessmanagement.service.impl;

import com.smartbusiness.businessmanagement.dto.request.RegisterRequest;
import com.smartbusiness.businessmanagement.dto.response.RegisterResponse;
import com.smartbusiness.businessmanagement.entity.Role;
import com.smartbusiness.businessmanagement.entity.User;
import com.smartbusiness.businessmanagement.repository.RoleRepository;
import com.smartbusiness.businessmanagement.repository.UserRepository;
import com.smartbusiness.businessmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public RegisterResponse createUser(RegisterRequest request) {

        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already exists");
        }

        Role role = roleRepository.findByName(request.role().toUpperCase())
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid role: " + request.role())
                );

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .enabled(true)
                .passwordChangeRequired(true)
                .build();

        user.getRoles().add(role);

        User savedUser = userRepository.save(user);

        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail()
        );
    }
}