package com.gallery.controller;

import com.gallery.common.PageResult;
import com.gallery.common.Result;
import com.gallery.entity.Favorite;
import com.gallery.security.LoginUser;
import com.gallery.service.FavoriteService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    private LoginUser getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof LoginUser)) {
            throw new RuntimeException("未登录或登录已过期");
        }
        return (LoginUser) auth.getPrincipal();
    }

    @GetMapping
    public Result<PageResult<Favorite>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Long userId = getCurrentUser().getId();
        PageResult<Favorite> pageResult = favoriteService.listFavorites(page, size, userId);
        return Result.ok(pageResult);
    }

    @GetMapping("/all")
    public Result<PageResult<Favorite>> listAll(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        PageResult<Favorite> pageResult = favoriteService.listAllFavorites(page, size);
        return Result.ok(pageResult);
    }

    @PostMapping("/{exhibitionId}")
    public Result<Void> toggle(@PathVariable Long exhibitionId) {
        Long userId = getCurrentUser().getId();
        favoriteService.toggle(userId, exhibitionId);
        return Result.ok();
    }

    @GetMapping("/{exhibitionId}/check")
    public Result<Boolean> check(@PathVariable Long exhibitionId) {
        Long userId = getCurrentUser().getId();
        boolean favorited = favoriteService.isFavorited(userId, exhibitionId);
        return Result.ok(favorited);
    }
}
