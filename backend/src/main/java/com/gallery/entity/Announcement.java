package com.gallery.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("announcement")
public class Announcement {

    private Long id;
    private String title;
    private String content;
    private String coverImage;
    /** 0草稿 1已发布 */
    private Integer status;
    /** 0否 1是 */
    private Integer topFlag;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;
}
