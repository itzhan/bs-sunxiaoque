package com.gallery.service;

import com.gallery.common.PageResult;
import com.gallery.entity.Favorite;

public interface FavoriteService {

    PageResult<Favorite> listFavorites(Integer page, Integer size, Long userId);

    PageResult<Favorite> listAllFavorites(Integer page, Integer size);

    void toggle(Long userId, Long exhibitionId);

    boolean isFavorited(Long userId, Long exhibitionId);
}
