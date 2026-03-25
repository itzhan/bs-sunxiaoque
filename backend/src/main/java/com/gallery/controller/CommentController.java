package com.gallery.controller;

import com.gallery.common.PageResult;
import com.gallery.common.Result;
import com.gallery.dto.CommentDTO;
import com.gallery.entity.Comment;
import com.gallery.security.LoginUser;
import com.gallery.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    private LoginUser getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof LoginUser)) {
            throw new RuntimeException("未登录或登录已过期");
        }
        return (LoginUser) auth.getPrincipal();
    }

    @GetMapping
    public Result<PageResult<Comment>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long exhibitionId,
            @RequestParam(required = false) Integer status) {
        PageResult<Comment> pageResult = commentService.listComments(page, size, exhibitionId, status);
        return Result.ok(pageResult);
    }

    @PutMapping("/{id}/approve")
    public Result<Void> approve(@PathVariable Long id) {
        commentService.approve(id);
        return Result.ok();
    }

    @PutMapping("/{id}/reject")
    public Result<Void> reject(@PathVariable Long id) {
        commentService.reject(id);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        commentService.delete(id);
        return Result.ok();
    }

    @PostMapping
    public Result<Void> create(@Valid @RequestBody CommentDTO dto) {
        Long userId = getCurrentUser().getId();
        commentService.create(userId, dto);
        return Result.ok();
    }
}
