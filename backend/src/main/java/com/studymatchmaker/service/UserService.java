package com.studymatchmaker.service;

import com.studymatchmaker.dto.MatchRequest;
import com.studymatchmaker.dto.MatchResult;
import com.studymatchmaker.dto.UpdateProfileRequest;
import com.studymatchmaker.dto.UserProfileDto;
import com.studymatchmaker.exception.NotFoundException;
import com.studymatchmaker.model.User;
import com.studymatchmaker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final MapperService mapperService;
    private final MatchingService matchingService;

    @Transactional(readOnly = true)
    public UserProfileDto getCurrentUser(Principal principal) {
        return mapperService.toUserProfile(getRequiredUser(principal));
    }

    @Transactional
    public UserProfileDto updateCurrentUser(Principal principal, UpdateProfileRequest request) {
        User user = getRequiredUser(principal);

        if (request.getFullName() != null) user.setFullName(request.getFullName());
        if (request.getSchool() != null) user.setSchool(request.getSchool());
        if (request.getMajor() != null) user.setMajor(request.getMajor());
        if (request.getAcademicLevel() != null) user.setAcademicLevel(request.getAcademicLevel());
        if (request.getBio() != null) user.setBio(request.getBio());
        if (request.getStudyStyle() != null) user.setStudyStyle(request.getStudyStyle());
        if (request.getPreferredMode() != null) user.setPreferredMode(request.getPreferredMode());
        if (request.getCourses() != null) user.setCourses(new HashSet<>(request.getCourses()));
        if (request.getLocationVisible() != null) user.setLocationVisible(request.getLocationVisible());
        if (request.getCity() != null) user.setCity(request.getCity());
        if (request.getState() != null) user.setState(request.getState());
        if (request.getLatitude() != null) user.setLatitude(request.getLatitude());
        if (request.getLongitude() != null) user.setLongitude(request.getLongitude());

        return mapperService.toUserProfile(userRepository.save(user));
    }

    @Transactional
    public UserProfileDto uploadProfilePicture(Principal principal, MultipartFile file) {
        User user = getRequiredUser(principal);
        user.setProfileImageUrl(file == null || file.isEmpty() ? null : file.getOriginalFilename());
        return mapperService.toUserProfile(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public List<UserProfileDto> getAllVisibleUsers() {
        return userRepository.findAll().stream()
                .map(mapperService::toUserProfile)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserProfileDto getUserById(Long id) {
        return mapperService.toUserProfile(userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found")));
    }

    @Transactional(readOnly = true)
    public List<MatchResult> matchUsers(Principal principal, MatchRequest request) {
        return matchingService.matchUsers(getRequiredUser(principal), request);
    }

    private User getRequiredUser(Principal principal) {
        String email = principal == null ? null : principal.getName();
        if (email == null || email.isBlank()) {
            throw new NotFoundException("Authenticated user not found");
        }
        return userRepository.findByEmail(email.toLowerCase().trim())
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
