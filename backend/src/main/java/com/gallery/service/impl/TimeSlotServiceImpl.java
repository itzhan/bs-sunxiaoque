package com.gallery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gallery.common.BusinessException;
import com.gallery.common.PageResult;
import com.gallery.dto.TimeSlotDTO;
import com.gallery.entity.Reservation;
import com.gallery.entity.TimeSlot;
import com.gallery.mapper.ReservationMapper;
import com.gallery.mapper.TimeSlotMapper;
import com.gallery.service.TimeSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class TimeSlotServiceImpl implements TimeSlotService {

    @Autowired
    private TimeSlotMapper timeSlotMapper;

    @Autowired
    private ReservationMapper reservationMapper;

    @Override
    public PageResult<TimeSlot> list(Integer page, Integer size, Long exhibitionId) {
        LambdaQueryWrapper<TimeSlot> wrapper = new LambdaQueryWrapper<>();
        if (exhibitionId != null) {
            wrapper.eq(TimeSlot::getExhibitionId, exhibitionId);
        }
        wrapper.orderByAsc(TimeSlot::getStartTime);

        IPage<TimeSlot> result = timeSlotMapper.selectPage(new Page<>(page, size), wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), page, size);
    }

    @Override
    public List<TimeSlot> listByExhibitionId(Long exhibitionId) {
        return timeSlotMapper.selectList(
                new LambdaQueryWrapper<TimeSlot>()
                        .eq(TimeSlot::getExhibitionId, exhibitionId)
                        .orderByAsc(TimeSlot::getStartTime));
    }

    @Override
    public TimeSlot getById(Long id) {
        TimeSlot slot = timeSlotMapper.selectById(id);
        if (slot == null) {
            throw new BusinessException("时间段不存在");
        }
        return slot;
    }

    @Override
    public void create(TimeSlotDTO dto) {
        TimeSlot slot = new TimeSlot();
        slot.setExhibitionId(dto.getExhibitionId());
        slot.setSlotName(dto.getSlotName());
        slot.setStartTime(dto.getStartTime());
        slot.setEndTime(dto.getEndTime());
        slot.setMaxCapacity(dto.getMaxCapacity() != null ? dto.getMaxCapacity() : 100);
        timeSlotMapper.insert(slot);
    }

    @Override
    public void update(Long id, TimeSlotDTO dto) {
        TimeSlot slot = timeSlotMapper.selectById(id);
        if (slot == null) {
            throw new BusinessException("时间段不存在");
        }
        slot.setExhibitionId(dto.getExhibitionId());
        slot.setSlotName(dto.getSlotName());
        slot.setStartTime(dto.getStartTime());
        slot.setEndTime(dto.getEndTime());
        slot.setMaxCapacity(dto.getMaxCapacity());
        timeSlotMapper.updateById(slot);
    }

    @Override
    public void delete(Long id) {
        timeSlotMapper.deleteById(id);
    }

    @Override
    public Integer getAvailableCapacity(Long timeSlotId, LocalDate date) {
        TimeSlot slot = timeSlotMapper.selectById(timeSlotId);
        if (slot == null) {
            throw new BusinessException("时间段不存在");
        }
        Integer usedCapacity = getUsedCapacity(timeSlotId, date);
        return slot.getMaxCapacity() - usedCapacity;
    }

    private Integer getUsedCapacity(Long timeSlotId, LocalDate date) {
        List<Reservation> reservations = reservationMapper.selectList(
                new LambdaQueryWrapper<Reservation>()
                        .eq(Reservation::getTimeSlotId, timeSlotId)
                        .eq(Reservation::getReservationDate, date)
                        .in(Reservation::getStatus, Arrays.asList(0, 1)));

        return reservations.stream()
                .mapToInt(Reservation::getNumVisitors)
                .sum();
    }
}
