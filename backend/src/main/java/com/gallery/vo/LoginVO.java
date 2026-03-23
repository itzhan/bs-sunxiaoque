package com.gallery.vo;

import lombok.Data;

@Data
public class LoginVO {

    private String token;
    private Long userId;
    private String username;
    private String nickname;
    private String avatar;
    private Integer role;
}
