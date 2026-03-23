package com.gallery.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_user")
public class User {

    private Long id;
    private String username;
    @JsonIgnore
    private String password;
    private String nickname;
    private String phone;
    private String email;
    private String avatar;
    /** 0未知 1男 2女 */
    private Integer gender;
    /** 0管理员 1普通用户 */
    private Integer role;
    /** 0禁用 1正常 */
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    @JsonIgnore
    @TableLogic
    private Integer deleted;
}
