package com.gallery.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("exhibit_item")
public class ExhibitItem {

    private Long id;
    private Long exhibitionId;
    private String name;
    private String artist;
    private String era;
    private String description;
    private String coverImage;
    private String audioUrl;
    private Integer sortOrder;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;
}
