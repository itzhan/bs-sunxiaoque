package com.gallery.controller;

import com.gallery.common.PageResult;
import com.gallery.common.Result;
import com.gallery.entity.*;
import com.gallery.service.*;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    private final ExhibitionService exhibitionService;
    private final ExhibitItemService exhibitItemService;
    private final VirtualTourService virtualTourService;
    private final TimeSlotService timeSlotService;
    private final AnnouncementService announcementService;
    private final ExhibitionCategoryService categoryService;
    private final CommentService commentService;

    public PublicController(ExhibitionService exhibitionService,
                            ExhibitItemService exhibitItemService,
                            VirtualTourService virtualTourService,
                            TimeSlotService timeSlotService,
                            AnnouncementService announcementService,
                            ExhibitionCategoryService categoryService,
                            CommentService commentService) {
        this.exhibitionService = exhibitionService;
        this.exhibitItemService = exhibitItemService;
        this.virtualTourService = virtualTourService;
        this.timeSlotService = timeSlotService;
        this.announcementService = announcementService;
        this.categoryService = categoryService;
        this.commentService = commentService;
    }

    @GetMapping("/exhibitions")
    public Result<PageResult<Exhibition>> listExhibitions(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long categoryId) {
        PageResult<Exhibition> pageResult = exhibitionService.listExhibitions(page, size, title, categoryId, null);
        return Result.ok(pageResult);
    }

    @GetMapping("/exhibitions/{id}")
    public Result<Map<String, Object>> getExhibitionDetail(@PathVariable Long id) {
        Exhibition exhibition = exhibitionService.getDetailById(id);
        List<ExhibitItem> items = exhibitItemService.listByExhibitionId(id);
        List<VirtualTour> tours = virtualTourService.listByExhibitionId(id);
        List<TimeSlot> slots = timeSlotService.listByExhibitionId(id);

        Map<String, Object> data = new HashMap<>();
        data.put("exhibition", exhibition);
        data.put("items", items);
        data.put("tours", tours);
        data.put("slots", slots);
        return Result.ok(data);
    }

    @GetMapping("/announcements")
    public Result<PageResult<Announcement>> listAnnouncements(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        PageResult<Announcement> pageResult = announcementService.list(page, size, 1);
        return Result.ok(pageResult);
    }

    @GetMapping("/announcements/{id}")
    public Result<Announcement> getAnnouncementDetail(@PathVariable Long id) {
        Announcement announcement = announcementService.getById(id);
        return Result.ok(announcement);
    }

    @GetMapping("/categories")
    public Result<List<ExhibitionCategory>> listCategories() {
        List<ExhibitionCategory> list = categoryService.listAll();
        return Result.ok(list);
    }

    @GetMapping("/exhibitions/{id}/comments")
    public Result<PageResult<Comment>> listComments(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        PageResult<Comment> pageResult = commentService.listComments(page, size, id, 1);
        return Result.ok(pageResult);
    }
}
