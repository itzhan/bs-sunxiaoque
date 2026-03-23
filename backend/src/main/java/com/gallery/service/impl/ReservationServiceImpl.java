package com.gallery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gallery.common.BusinessException;
import com.gallery.common.PageResult;
import com.gallery.dto.ReservationDTO;
import com.gallery.entity.Comment;
import com.gallery.entity.Exhibition;
import com.gallery.entity.Reservation;
import com.gallery.entity.TimeSlot;
import com.gallery.entity.User;
import com.gallery.mapper.CommentMapper;
import com.gallery.mapper.ExhibitionMapper;
import com.gallery.mapper.ReservationMapper;
import com.gallery.mapper.TimeSlotMapper;
import com.gallery.mapper.UserMapper;
import com.gallery.service.ReservationService;
import com.gallery.vo.DashboardVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationMapper reservationMapper;

    @Autowired
    private TimeSlotMapper timeSlotMapper;

    @Autowired
    private ExhibitionMapper exhibitionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public PageResult<Reservation> listReservations(Integer page, Integer size,
                                                     Long userId, Long exhibitionId,
                                                     Integer status, String username) {
        IPage<Reservation> result = reservationMapper.selectPageWithDetails(
                new Page<>(page, size), userId, exhibitionId, status, username);
        return new PageResult<>(result.getRecords(), result.getTotal(), page, size);
    }

    @Override
    public Reservation getById(Long id) {
        Reservation reservation = reservationMapper.selectById(id);
        if (reservation == null) {
            throw new BusinessException("预约不存在");
        }
        return reservation;
    }

    @Override
    public void create(Long userId, ReservationDTO dto) {
        TimeSlot slot = timeSlotMapper.selectById(dto.getTimeSlotId());
        if (slot == null) {
            throw new BusinessException("时间段不存在");
        }

        int usedCapacity = getUsedCapacity(dto.getTimeSlotId(), dto.getReservationDate());
        int available = slot.getMaxCapacity() - usedCapacity;
        if (dto.getNumVisitors() > available) {
            throw new BusinessException("该时间段剩余容量不足，当前可预约人数：" + available);
        }

        Reservation reservation = new Reservation();
        reservation.setUserId(userId);
        reservation.setExhibitionId(dto.getExhibitionId());
        reservation.setTimeSlotId(dto.getTimeSlotId());
        reservation.setReservationDate(dto.getReservationDate());
        reservation.setNumVisitors(dto.getNumVisitors());
        reservation.setContactName(dto.getContactName());
        reservation.setContactPhone(dto.getContactPhone());
        reservation.setStatus(0);
        reservationMapper.insert(reservation);
    }

    @Override
    public void cancel(Long userId, Long id, String reason) {
        Reservation reservation = reservationMapper.selectById(id);
        if (reservation == null) {
            throw new BusinessException("预约不存在");
        }
        if (userId != null && !reservation.getUserId().equals(userId)) {
            throw new BusinessException("无权取消该预约");
        }
        if (reservation.getStatus() != 0 && reservation.getStatus() != 1) {
            throw new BusinessException("当前状态不可取消");
        }
        reservation.setStatus(2);
        reservation.setCancelReason(reason);
        reservationMapper.updateById(reservation);
    }

    @Override
    public void confirm(Long id) {
        Reservation reservation = reservationMapper.selectById(id);
        if (reservation == null) {
            throw new BusinessException("预约不存在");
        }
        if (reservation.getStatus() != 0) {
            throw new BusinessException("只能确认待确认的预约");
        }
        reservation.setStatus(1);
        reservationMapper.updateById(reservation);
    }

    @Override
    public void complete(Long id) {
        Reservation reservation = reservationMapper.selectById(id);
        if (reservation == null) {
            throw new BusinessException("预约不存在");
        }
        if (reservation.getStatus() != 1) {
            throw new BusinessException("只能完成已确认的预约");
        }
        reservation.setStatus(3);
        reservationMapper.updateById(reservation);
    }

    @Override
    public DashboardVO getDashboard() {
        DashboardVO vo = new DashboardVO();
        vo.setTotalUsers(userMapper.selectCount(null));
        vo.setTotalExhibitions(exhibitionMapper.selectCount(null));
        vo.setTotalReservations(reservationMapper.selectCount(null));

        vo.setTodayReservations(reservationMapper.selectCount(
                new LambdaQueryWrapper<Reservation>()
                        .eq(Reservation::getReservationDate, LocalDate.now())));

        vo.setTotalComments(commentMapper.selectCount(null));

        List<Exhibition> exhibitions = exhibitionMapper.selectList(null);
        long totalVisits = exhibitions.stream()
                .mapToLong(e -> e.getViewCount() != null ? e.getViewCount() : 0)
                .sum();
        vo.setTotalVisits(totalVisits);

        return vo;
    }

    private int getUsedCapacity(Long timeSlotId, LocalDate date) {
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
