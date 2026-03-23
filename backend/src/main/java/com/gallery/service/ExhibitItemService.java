package com.gallery.service;

import com.gallery.common.PageResult;
import com.gallery.dto.ExhibitItemDTO;
import com.gallery.entity.ExhibitItem;

import java.util.List;

public interface ExhibitItemService {

    PageResult<ExhibitItem> list(Integer page, Integer size, Long exhibitionId);

    List<ExhibitItem> listByExhibitionId(Long exhibitionId);

    ExhibitItem getById(Long id);

    void create(ExhibitItemDTO dto);

    void update(Long id, ExhibitItemDTO dto);

    void delete(Long id);
}
