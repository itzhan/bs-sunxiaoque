package com.gallery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gallery.common.BusinessException;
import com.gallery.common.PageResult;
import com.gallery.dto.VirtualTourDTO;
import com.gallery.entity.VirtualTour;
import com.gallery.mapper.VirtualTourMapper;
import com.gallery.service.VirtualTourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VirtualTourServiceImpl implements VirtualTourService {

    @Autowired
    private VirtualTourMapper virtualTourMapper;

    @Override
    public PageResult<VirtualTour> list(Integer page, Integer size, Long exhibitionId) {
        LambdaQueryWrapper<VirtualTour> wrapper = new LambdaQueryWrapper<>();
        if (exhibitionId != null) {
            wrapper.eq(VirtualTour::getExhibitionId, exhibitionId);
        }
        wrapper.orderByAsc(VirtualTour::getSortOrder);

        IPage<VirtualTour> result = virtualTourMapper.selectPage(new Page<>(page, size), wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), page, size);
    }

    @Override
    public List<VirtualTour> listByExhibitionId(Long exhibitionId) {
        return virtualTourMapper.selectList(
                new LambdaQueryWrapper<VirtualTour>()
                        .eq(VirtualTour::getExhibitionId, exhibitionId)
                        .orderByAsc(VirtualTour::getSortOrder));
    }

    @Override
    public VirtualTour getById(Long id) {
        VirtualTour tour = virtualTourMapper.selectById(id);
        if (tour == null) {
            throw new BusinessException("虚拟导览不存在");
        }
        return tour;
    }

    @Override
    public void create(VirtualTourDTO dto) {
        VirtualTour tour = new VirtualTour();
        tour.setExhibitionId(dto.getExhibitionId());
        tour.setTitle(dto.getTitle());
        tour.setDescription(dto.getDescription());
        tour.setPanoramaUrl(dto.getPanoramaUrl());
        tour.setThumbnail(dto.getThumbnail());
        tour.setTourType(dto.getTourType() != null ? dto.getTourType() : 0);
        tour.setSortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : 0);
        virtualTourMapper.insert(tour);
    }

    @Override
    public void update(Long id, VirtualTourDTO dto) {
        VirtualTour tour = virtualTourMapper.selectById(id);
        if (tour == null) {
            throw new BusinessException("虚拟导览不存在");
        }
        tour.setExhibitionId(dto.getExhibitionId());
        tour.setTitle(dto.getTitle());
        tour.setDescription(dto.getDescription());
        tour.setPanoramaUrl(dto.getPanoramaUrl());
        tour.setThumbnail(dto.getThumbnail());
        tour.setTourType(dto.getTourType());
        tour.setSortOrder(dto.getSortOrder());
        virtualTourMapper.updateById(tour);
    }

    @Override
    public void delete(Long id) {
        virtualTourMapper.deleteById(id);
    }
}
