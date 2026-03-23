package com.gallery.service;

import com.gallery.common.PageResult;
import com.gallery.dto.ReservationDTO;
import com.gallery.entity.Reservation;
import com.gallery.vo.DashboardVO;

public interface ReservationService {

    PageResult<Reservation> listReservations(Integer page, Integer size,
                                              Long userId, Long exhibitionId,
                                              Integer status, String username);

    Reservation getById(Long id);

    void create(Long userId, ReservationDTO dto);

    void cancel(Long userId, Long id, String reason);

    void confirm(Long id);

    void complete(Long id);

    DashboardVO getDashboard();
}
