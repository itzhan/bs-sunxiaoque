package com.gallery.controller;

import com.gallery.common.PageResult;
import com.gallery.common.Result;
import com.gallery.dto.ReservationDTO;
import com.gallery.entity.Reservation;
import com.gallery.security.LoginUser;
import com.gallery.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    private LoginUser getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (LoginUser) auth.getPrincipal();
    }

    @GetMapping
    public Result<PageResult<Reservation>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long exhibitionId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String username) {
        PageResult<Reservation> pageResult = reservationService.listReservations(
                page, size, null, exhibitionId, status, username);
        return Result.ok(pageResult);
    }

    @PutMapping("/{id}/confirm")
    public Result<Void> confirm(@PathVariable Long id) {
        reservationService.confirm(id);
        return Result.ok();
    }

    @PutMapping("/{id}/complete")
    public Result<Void> complete(@PathVariable Long id) {
        reservationService.complete(id);
        return Result.ok();
    }

    @GetMapping("/my")
    public Result<PageResult<Reservation>> myReservations(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer status) {
        Long userId = getCurrentUser().getId();
        PageResult<Reservation> pageResult = reservationService.listReservations(
                page, size, userId, null, status, null);
        return Result.ok(pageResult);
    }

    @PostMapping
    public Result<Void> create(@Valid @RequestBody ReservationDTO dto) {
        Long userId = getCurrentUser().getId();
        reservationService.create(userId, dto);
        return Result.ok();
    }

    @PutMapping("/{id}/cancel")
    public Result<Void> cancel(@PathVariable Long id,
                               @RequestBody(required = false) Map<String, String> body) {
        Long userId = getCurrentUser().getId();
        String reason = (body != null) ? body.get("reason") : null;
        reservationService.cancel(userId, id, reason);
        return Result.ok();
    }
}
