package com.gallery.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("favorite")
public class Favorite {

    private Long id;
    private Long userId;
    private Long exhibitionId;
    private LocalDateTime createTime;

    @TableField(exist = false)
    private String exhibitionTitle;
    @TableField(exist = false)
    private String coverImage;
}
