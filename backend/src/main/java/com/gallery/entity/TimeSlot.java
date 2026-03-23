package com.gallery.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@TableName("time_slot")
public class TimeSlot {

    private Long id;
    private Long exhibitionId;
    private String slotName;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer maxCapacity;
    private LocalDateTime createTime;
    @TableLogic
    private Integer deleted;
}
