package com.studymatchmaker.service;

import com.studymatchmaker.dto.AuthResponse;
import com.studymatchmaker.dto.LoginRequest;
import com.studymatchmaker.dto.RegisterRequest;
import com.studymatchmaker.exception.BadRequestException;
import com.studymatchmaker.model.User;
import com.studymatchmaker.repository.UserRepository;
import com.studymatchmaker.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final MapperService mapperService;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail().toLowerCase().trim())) {
            throw new BadRequestException("Email already registered");
        }
        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail().toLowerCase().trim())
                .password(passwordEncoder.encode(request.getPassword()))
                .state(request.getState())
                .school(request.getSchool())
                .major(request.getMajor())
                .academicLevel(request.getAcademicLevel())
                .studyStyle(request.getStudyStyle())
                .preferredMode(request.getPreferredMode() == null ? "Hybrid" : request.getPreferredMode())
                .courses(request.getCourses() == null ? new HashSet<>() : new HashSet<>(request.getCourses()))
                .locationVisible(request.getLocationVisible() == null ? Boolean.TRUE : request.getLocationVisible())
                .city(request.getCity())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .bio(request.getBio())
                .build();
        User saved = userRepository.save(user);
        return AuthResponse.builder().token(jwtService.generateToken(saved)).user(mapperService.toUserProfile(saved)).build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new BadRequestException("Invalid credentials"));
        return AuthResponse.builder().token(jwtService.generateToken(user)).user(mapperService.toUserProfile(user)).build();
    }
}
