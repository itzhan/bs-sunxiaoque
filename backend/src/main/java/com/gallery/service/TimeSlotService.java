package com.gallery.service;

import com.gallery.common.PageResult;
import com.gallery.dto.TimeSlotDTO;
import com.gallery.entity.TimeSlot;

import java.time.LocalDate;
import java.util.List;

public interface TimeSlotService {

    PageResult<TimeSlot> list(Integer page, Integer size, Long exhibitionId);

    List<TimeSlot> listByExhibitionId(Long exhibitionId);

    TimeSlot getById(Long id);

    void create(TimeSlotDTO dto);

    void update(Long id, TimeSlotDTO dto);

    void delete(Long id);

    Integer getAvailableCapacity(Long timeSlotId, LocalDate date);
}
