package com.gallery.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("comment")
public class Comment {

    private Long id;
    private Long userId;
    private Long exhibitionId;
    private String content;
    /** 1-5 */
    private Integer rating;
    /** 0待审核 1已通过 2已拒绝 */
    private Integer status;
    private LocalDateTime createTime;
    @TableLogic
    private Integer deleted;

    @TableField(exist = false)
    private String nickname;
    @TableField(exist = false)
    private String avatar;
    @TableField(exist = false)
    private String exhibitionTitle;
}
