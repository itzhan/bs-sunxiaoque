package com.gallery.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("exhibition")
public class Exhibition {

    private Long id;
    private String title;
    private String description;
    private String coverImage;
    private String images;
    private Long categoryId;
    private String location;
    private String hallName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer dailyCapacity;
    private BigDecimal ticketPrice;
    /** 0草稿 1即将开展 2展出中 3已结束 */
    private Integer status;
    private Integer viewCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;

    @TableField(exist = false)
    private String categoryName;
}
