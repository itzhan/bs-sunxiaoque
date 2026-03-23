package com.gallery.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gallery.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    IPage<Comment> selectPageWithUser(IPage<Comment> page,
                                      @Param("exhibitionId") Long exhibitionId,
                                      @Param("status") Integer status);
}
