package com.gallery.controller;

import com.gallery.common.PageResult;
import com.gallery.common.Result;
import com.gallery.dto.ExhibitItemDTO;
import com.gallery.entity.ExhibitItem;
import com.gallery.service.ExhibitItemService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exhibit-items")
public class ExhibitItemController {

    private final ExhibitItemService exhibitItemService;

    public ExhibitItemController(ExhibitItemService exhibitItemService) {
        this.exhibitItemService = exhibitItemService;
    }

    @GetMapping
    public Result<PageResult<ExhibitItem>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long exhibitionId) {
        PageResult<ExhibitItem> pageResult = exhibitItemService.list(page, size, exhibitionId);
        return Result.ok(pageResult);
    }

    @GetMapping("/{id}")
    public Result<ExhibitItem> getById(@PathVariable Long id) {
        ExhibitItem item = exhibitItemService.getById(id);
        return Result.ok(item);
    }

    @PostMapping
    public Result<Void> create(@Valid @RequestBody ExhibitItemDTO dto) {
        exhibitItemService.create(dto);
        return Result.ok();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody ExhibitItemDTO dto) {
        exhibitItemService.update(id, dto);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        exhibitItemService.delete(id);
        return Result.ok();
    }
}
