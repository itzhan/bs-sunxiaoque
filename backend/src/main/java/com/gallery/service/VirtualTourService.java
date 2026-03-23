package com.gallery.service;

import com.gallery.common.PageResult;
import com.gallery.dto.VirtualTourDTO;
import com.gallery.entity.VirtualTour;

import java.util.List;

public interface VirtualTourService {

    PageResult<VirtualTour> list(Integer page, Integer size, Long exhibitionId);

    List<VirtualTour> listByExhibitionId(Long exhibitionId);

    VirtualTour getById(Long id);

    void create(VirtualTourDTO dto);

    void update(Long id, VirtualTourDTO dto);

    void delete(Long id);
}
