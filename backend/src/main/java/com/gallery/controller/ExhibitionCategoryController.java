package com.gallery.controller;

import com.gallery.common.PageResult;
import com.gallery.common.Result;
import com.gallery.dto.CategoryDTO;
import com.gallery.entity.ExhibitionCategory;
import com.gallery.service.ExhibitionCategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class ExhibitionCategoryController {

    private final ExhibitionCategoryService categoryService;

    public ExhibitionCategoryController(ExhibitionCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public Result<PageResult<ExhibitionCategory>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        PageResult<ExhibitionCategory> pageResult = categoryService.list(page, size, keyword, null);
        return Result.ok(pageResult);
    }

    @GetMapping("/all")
    public Result<List<ExhibitionCategory>> listAll() {
        List<ExhibitionCategory> list = categoryService.listAll();
        return Result.ok(list);
    }

    @GetMapping("/{id}")
    public Result<ExhibitionCategory> getById(@PathVariable Long id) {
        ExhibitionCategory category = categoryService.getById(id);
        return Result.ok(category);
    }

    @PostMapping
    public Result<Void> create(@Valid @RequestBody CategoryDTO dto) {
        categoryService.create(dto);
        return Result.ok();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody CategoryDTO dto) {
        categoryService.update(id, dto);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return Result.ok();
    }
}
