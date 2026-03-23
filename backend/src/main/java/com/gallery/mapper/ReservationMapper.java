package com.gallery.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gallery.entity.Reservation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReservationMapper extends BaseMapper<Reservation> {

    IPage<Reservation> selectPageWithDetails(IPage<Reservation> page,
                                             @Param("userId") Long userId,
                                             @Param("exhibitionId") Long exhibitionId,
                                             @Param("status") Integer status,
                                             @Param("username") String username);
}
