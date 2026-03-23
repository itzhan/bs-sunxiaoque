package com.gallery.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gallery.entity.Exhibition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ExhibitionMapper extends BaseMapper<Exhibition> {

    IPage<Exhibition> selectPageWithCategory(IPage<Exhibition> page,
                                             @Param("title") String title,
                                             @Param("categoryId") Long categoryId,
                                             @Param("status") Integer status);
}
