package com.gallery.service;

import com.gallery.common.PageResult;
import com.gallery.dto.LoginDTO;
import com.gallery.dto.PasswordDTO;
import com.gallery.dto.RegisterDTO;
import com.gallery.dto.UserUpdateDTO;
import com.gallery.entity.User;
import com.gallery.vo.LoginVO;

public interface UserService {

    LoginVO login(LoginDTO dto);

    LoginVO register(RegisterDTO dto);

    User getById(Long id);

    void updateUser(Long id, UserUpdateDTO dto);

    void changePassword(Long id, PasswordDTO dto);

    PageResult<User> listUsers(Integer page, Integer size, String keyword, Integer status);

    void updateStatus(Long id, Integer status);

    void resetPassword(Long id);
}
