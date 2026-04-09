package com.studymatchmaker.service;

import com.studymatchmaker.dto.UserProfileDto;
import com.studymatchmaker.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MapperService {
    public UserProfileDto toUserProfile(User user) {
        return UserProfileDto.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .school(user.getSchool())
                .major(user.getMajor())
                .academicLevel(user.getAcademicLevel())
                .bio(user.getBio())
                .studyStyle(user.getStudyStyle())
                .preferredMode(user.getPreferredMode())
                .courses(new ArrayList<>(user.getCourses()))
                .locationVisible(user.getLocationVisible())
                .city(user.getCity())
                .state(user.getState())
                .latitude(user.getLatitude())
                .longitude(user.getLongitude())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }
}
