package com.gallery.controller;

import com.gallery.common.Result;
import com.gallery.dto.LoginDTO;
import com.gallery.dto.RegisterDTO;
import com.gallery.entity.User;
import com.gallery.security.LoginUser;
import com.gallery.service.UserService;
import com.gallery.vo.LoginVO;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    private LoginUser getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof LoginUser)) {
            throw new RuntimeException("未登录或登录已过期");
        }
        return (LoginUser) auth.getPrincipal();
    }

    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO loginDTO) {
        LoginVO vo = userService.login(loginDTO);
        return Result.ok(vo);
    }

    @PostMapping("/register")
    public Result<LoginVO> register(@Valid @RequestBody RegisterDTO registerDTO) {
        LoginVO vo = userService.register(registerDTO);
        return Result.ok(vo);
    }

    @GetMapping("/info")
    public Result<User> info() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof LoginUser)) {
            return Result.error(401, "未登录或登录已过期");
        }
        LoginUser loginUser = (LoginUser) auth.getPrincipal();
        Long userId = loginUser.getId();
        User user = userService.getById(userId);
        return Result.ok(user);
    }
}
