package com.gallery.controller;

import com.gallery.common.PageResult;
import com.gallery.common.Result;
import com.gallery.dto.VirtualTourDTO;
import com.gallery.entity.VirtualTour;
import com.gallery.service.VirtualTourService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/virtual-tours")
public class VirtualTourController {

    private final VirtualTourService virtualTourService;

    public VirtualTourController(VirtualTourService virtualTourService) {
        this.virtualTourService = virtualTourService;
    }

    @GetMapping
    public Result<PageResult<VirtualTour>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long exhibitionId) {
        PageResult<VirtualTour> pageResult = virtualTourService.list(page, size, exhibitionId);
        return Result.ok(pageResult);
    }

    @GetMapping("/{id}")
    public Result<VirtualTour> getById(@PathVariable Long id) {
        VirtualTour tour = virtualTourService.getById(id);
        return Result.ok(tour);
    }

    @PostMapping
    public Result<Void> create(@Valid @RequestBody VirtualTourDTO dto) {
        virtualTourService.create(dto);
        return Result.ok();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody VirtualTourDTO dto) {
        virtualTourService.update(id, dto);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        virtualTourService.delete(id);
        return Result.ok();
    }
}
