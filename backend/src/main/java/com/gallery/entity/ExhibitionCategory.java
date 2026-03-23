package com.gallery.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("exhibition_category")
public class ExhibitionCategory {

    private Long id;
    private String name;
    private String description;
    private Integer sortOrder;
    /** 0禁用 1正常 */
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;
}
