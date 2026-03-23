package com.gallery.controller;

import com.gallery.common.Result;
import com.gallery.dto.TimeSlotDTO;
import com.gallery.entity.TimeSlot;
import com.gallery.service.TimeSlotService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/time-slots")
public class TimeSlotController {

    private final TimeSlotService timeSlotService;

    public TimeSlotController(TimeSlotService timeSlotService) {
        this.timeSlotService = timeSlotService;
    }

    @GetMapping
    public Result<List<TimeSlot>> list(@RequestParam Long exhibitionId) {
        List<TimeSlot> list = timeSlotService.listByExhibitionId(exhibitionId);
        return Result.ok(list);
    }

    @GetMapping("/{id}")
    public Result<TimeSlot> getById(@PathVariable Long id) {
        TimeSlot slot = timeSlotService.getById(id);
        return Result.ok(slot);
    }

    @PostMapping
    public Result<Void> create(@Valid @RequestBody TimeSlotDTO dto) {
        timeSlotService.create(dto);
        return Result.ok();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody TimeSlotDTO dto) {
        timeSlotService.update(id, dto);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        timeSlotService.delete(id);
        return Result.ok();
    }

    @GetMapping("/{id}/available")
    public Result<Integer> getAvailableCapacity(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Integer capacity = timeSlotService.getAvailableCapacity(id, date);
        return Result.ok(capacity);
    }
}
