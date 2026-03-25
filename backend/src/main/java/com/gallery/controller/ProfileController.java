package com.gallery.controller;

import com.gallery.common.Result;
import com.gallery.dto.PasswordDTO;
import com.gallery.dto.UserUpdateDTO;
import com.gallery.entity.User;
import com.gallery.security.LoginUser;
import com.gallery.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    private LoginUser getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof LoginUser)) {
            throw new RuntimeException("未登录或登录已过期");
        }
        return (LoginUser) auth.getPrincipal();
    }

    @GetMapping
    public Result<User> getProfile() {
        Long userId = getCurrentUser().getId();
        User user = userService.getById(userId);
        return Result.ok(user);
    }

    @PutMapping
    public Result<Void> updateProfile(@Valid @RequestBody UserUpdateDTO dto) {
        Long userId = getCurrentUser().getId();
        userService.updateUser(userId, dto);
        return Result.ok();
    }

    @PutMapping("/password")
    public Result<Void> changePassword(@Valid @RequestBody PasswordDTO dto) {
        Long userId = getCurrentUser().getId();
        userService.changePassword(userId, dto);
        return Result.ok();
    }
}
