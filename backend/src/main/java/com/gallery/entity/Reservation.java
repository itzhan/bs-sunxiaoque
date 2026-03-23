package com.gallery.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("reservation")
public class Reservation {

    private Long id;
    private Long userId;
    private Long exhibitionId;
    private Long timeSlotId;
    private LocalDate reservationDate;
    private Integer numVisitors;
    private String contactName;
    private String contactPhone;
    /** 0待确认 1已确认 2已取消 3已完成 4已过期 */
    private Integer status;
    private String cancelReason;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;

    @TableField(exist = false)
    private String exhibitionTitle;
    @TableField(exist = false)
    private String slotName;
    @TableField(exist = false)
    private String username;
}
