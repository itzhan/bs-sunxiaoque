package com.gallery.controller;

import com.gallery.common.PageResult;
import com.gallery.common.Result;
import com.gallery.dto.ExhibitionDTO;
import com.gallery.entity.Exhibition;
import com.gallery.service.ExhibitionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exhibitions")
public class ExhibitionController {

    private final ExhibitionService exhibitionService;

    public ExhibitionController(ExhibitionService exhibitionService) {
        this.exhibitionService = exhibitionService;
    }

    @GetMapping
    public Result<PageResult<Exhibition>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer status) {
        PageResult<Exhibition> pageResult = exhibitionService.listExhibitions(page, size, title, categoryId, status);
        return Result.ok(pageResult);
    }

    @GetMapping("/{id}")
    public Result<Exhibition> getById(@PathVariable Long id) {
        Exhibition exhibition = exhibitionService.getDetailById(id);
        return Result.ok(exhibition);
    }

    @PostMapping
    public Result<Void> create(@Valid @RequestBody ExhibitionDTO dto) {
        exhibitionService.create(dto);
        return Result.ok();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody ExhibitionDTO dto) {
        exhibitionService.update(id, dto);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        exhibitionService.delete(id);
        return Result.ok();
    }
}
