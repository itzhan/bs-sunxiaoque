package com.gallery.service;

import com.gallery.common.PageResult;
import com.gallery.dto.ExhibitionDTO;
import com.gallery.entity.Exhibition;

public interface ExhibitionService {

    PageResult<Exhibition> listExhibitions(Integer page, Integer size, String title, Long categoryId, Integer status);

    Exhibition getById(Long id);

    Exhibition getDetailById(Long id);

    void create(ExhibitionDTO dto);

    void update(Long id, ExhibitionDTO dto);

    void delete(Long id);
}
