package com.gallery.service;

import com.gallery.common.PageResult;
import com.gallery.dto.CommentDTO;
import com.gallery.entity.Comment;

public interface CommentService {

    PageResult<Comment> listComments(Integer page, Integer size, Long exhibitionId, Integer status);

    void create(Long userId, CommentDTO dto);

    void approve(Long id);

    void reject(Long id);

    void delete(Long id);
}
