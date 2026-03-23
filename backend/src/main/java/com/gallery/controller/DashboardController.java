package com.gallery.controller;

import com.gallery.common.Result;
import com.gallery.service.ReservationService;
import com.gallery.vo.DashboardVO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final ReservationService reservationService;

    public DashboardController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public Result<DashboardVO> getDashboard() {
        DashboardVO vo = reservationService.getDashboard();
        return Result.ok(vo);
    }
}
