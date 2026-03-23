package com.studymatchmaker.controller;

import com.studymatchmaker.dto.MatchRequest;
import com.studymatchmaker.dto.MatchResult;
import com.studymatchmaker.dto.UpdateProfileRequest;
import com.studymatchmaker.dto.UserProfileDto;
import com.studymatchmaker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me") public UserProfileDto getMe(Principal principal) { return userService.getCurrentUser(principal); }
    @PutMapping("/me") public UserProfileDto updateMe(Principal principal, @RequestBody UpdateProfileRequest request) { return userService.updateCurrentUser(principal, request); }
    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE) public UserProfileDto uploadImage(Principal principal, @RequestPart("file") MultipartFile file) { return userService.uploadProfilePicture(principal, file); }
    @GetMapping public List<UserProfileDto> getUsers() { return userService.getAllVisibleUsers(); }
    @GetMapping("/{id}") public UserProfileDto getUser(@PathVariable Long id) { return userService.getUserById(id); }
    @PostMapping("/match") public List<MatchResult> match(Principal principal, @RequestBody(required = false) MatchRequest request) { return userService.matchUsers(principal, request == null ? new MatchRequest() : request); }
}
