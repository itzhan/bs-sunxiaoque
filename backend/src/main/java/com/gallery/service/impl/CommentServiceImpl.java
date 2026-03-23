package com.gallery.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gallery.common.BusinessException;
import com.gallery.common.PageResult;
import com.gallery.dto.CommentDTO;
import com.gallery.entity.Comment;
import com.gallery.mapper.CommentMapper;
import com.gallery.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public PageResult<Comment> listComments(Integer page, Integer size,
                                             Long exhibitionId, Integer status) {
        IPage<Comment> result = commentMapper.selectPageWithUser(
                new Page<>(page, size), exhibitionId, status);
        return new PageResult<>(result.getRecords(), result.getTotal(), page, size);
    }

    @Override
    public void create(Long userId, CommentDTO dto) {
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setExhibitionId(dto.getExhibitionId());
        comment.setContent(dto.getContent());
        comment.setRating(dto.getRating());
        comment.setStatus(0);
        commentMapper.insert(comment);
    }

    @Override
    public void approve(Long id) {
        Comment comment = commentMapper.selectById(id);
        if (comment == null) {
            throw new BusinessException("评论不存在");
        }
        comment.setStatus(1);
        commentMapper.updateById(comment);
    }

    @Override
    public void reject(Long id) {
        Comment comment = commentMapper.selectById(id);
        if (comment == null) {
            throw new BusinessException("评论不存在");
        }
        comment.setStatus(2);
        commentMapper.updateById(comment);
    }

    @Override
    public void delete(Long id) {
        commentMapper.deleteById(id);
    }
}
