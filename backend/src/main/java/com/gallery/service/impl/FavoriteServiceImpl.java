package com.gallery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gallery.common.PageResult;
import com.gallery.entity.Favorite;
import com.gallery.mapper.FavoriteMapper;
import com.gallery.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Override
    public PageResult<Favorite> listFavorites(Integer page, Integer size, Long userId) {
        IPage<Favorite> result = favoriteMapper.selectPageWithExhibition(
                new Page<>(page, size), userId);
        return new PageResult<>(result.getRecords(), result.getTotal(), page, size);
    }

    @Override
    public PageResult<Favorite> listAllFavorites(Integer page, Integer size) {
        IPage<Favorite> result = favoriteMapper.selectPageWithExhibition(
                new Page<>(page, size), null);
        return new PageResult<>(result.getRecords(), result.getTotal(), page, size);
    }

    @Override
    public void toggle(Long userId, Long exhibitionId) {
        Favorite existing = favoriteMapper.selectOne(
                new LambdaQueryWrapper<Favorite>()
                        .eq(Favorite::getUserId, userId)
                        .eq(Favorite::getExhibitionId, exhibitionId));

        if (existing != null) {
            favoriteMapper.deleteById(existing.getId());
        } else {
            Favorite favorite = new Favorite();
            favorite.setUserId(userId);
            favorite.setExhibitionId(exhibitionId);
            favoriteMapper.insert(favorite);
        }
    }

    @Override
    public boolean isFavorited(Long userId, Long exhibitionId) {
        return favoriteMapper.selectCount(
                new LambdaQueryWrapper<Favorite>()
                        .eq(Favorite::getUserId, userId)
                        .eq(Favorite::getExhibitionId, exhibitionId)) > 0;
    }
}
