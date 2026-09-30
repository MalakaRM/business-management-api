package com.smartbusiness.businessmanagement.service.impl;

import com.smartbusiness.businessmanagement.dto.request.LoginRequest;
import com.smartbusiness.businessmanagement.dto.request.RegisterRequest;
import com.smartbusiness.businessmanagement.dto.response.LoginResponse;
import com.smartbusiness.businessmanagement.dto.response.RegisterResponse;
import com.smartbusiness.businessmanagement.entity.Role;
import com.smartbusiness.businessmanagement.entity.User;
import com.smartbusiness.businessmanagement.exception.ResourceAlreadyExistsException;
import com.smartbusiness.businessmanagement.repository.RoleRepository;
import com.smartbusiness.businessmanagement.repository.UserRepository;
import com.smartbusiness.businessmanagement.service.AuthService;
import com.smartbusiness.businessmanagement.service.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponse login(LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.username(),
                                request.password()
                        )
                );

        UserDetails userDetails =
                (UserDetails) authentication.getPrincipal();

        String token = jwtService.generateToken(userDetails);

        Set<String> roles = userDetails.getAuthorities()
                .stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toSet());

        return new LoginResponse(
                token,
                "Bearer",
                userDetails.getUsername(),
                roles
        );
    }

    @Override
    public RegisterResponse register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.username())) {
            throw new ResourceAlreadyExistsException(
                    "Username is already registered"
            );
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new ResourceAlreadyExistsException(
                    "Email is already registered"
            );
        }

        Role employeeRole = roleRepository
                .findByName("EMPLOYEE")
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Default EMPLOYEE role not found"
                        )
                );

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .enabled(true)
                .roles(Set.of(employeeRole))
                .build();

        User savedUser = userRepository.save(user);

        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail()
        );
    }
}