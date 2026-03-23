package com.gallery.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("virtual_tour")
public class VirtualTour {

    private Long id;
    private Long exhibitionId;
    private String title;
    private String description;
    private String panoramaUrl;
    private String thumbnail;
    /** 0全景 1-3D */
    private Integer tourType;
    private Integer sortOrder;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;
}
