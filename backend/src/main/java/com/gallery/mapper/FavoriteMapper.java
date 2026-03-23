package com.gallery.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gallery.entity.Favorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FavoriteMapper extends BaseMapper<Favorite> {

    IPage<Favorite> selectPageWithExhibition(IPage<Favorite> page,
                                              @Param("userId") Long userId);
}
