package com.gallery.controller;

import com.gallery.common.PageResult;
import com.gallery.common.Result;
import com.gallery.dto.AnnouncementDTO;
import com.gallery.entity.Announcement;
import com.gallery.service.AnnouncementService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {

    private final AnnouncementService announcementService;

    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @GetMapping
    public Result<PageResult<Announcement>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer status) {
        PageResult<Announcement> pageResult = announcementService.list(page, size, status);
        return Result.ok(pageResult);
    }

    @GetMapping("/{id}")
    public Result<Announcement> getById(@PathVariable Long id) {
        Announcement announcement = announcementService.getById(id);
        return Result.ok(announcement);
    }

    @PostMapping
    public Result<Void> create(@Valid @RequestBody AnnouncementDTO dto) {
        announcementService.create(dto);
        return Result.ok();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody AnnouncementDTO dto) {
        announcementService.update(id, dto);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        announcementService.delete(id);
        return Result.ok();
    }
}
