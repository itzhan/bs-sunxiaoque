package com.gallery.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gallery.common.BusinessException;
import com.gallery.common.PageResult;
import com.gallery.dto.ExhibitionDTO;
import com.gallery.entity.Exhibition;
import com.gallery.mapper.ExhibitionMapper;
import com.gallery.service.ExhibitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExhibitionServiceImpl implements ExhibitionService {

    @Autowired
    private ExhibitionMapper exhibitionMapper;

    @Override
    public PageResult<Exhibition> listExhibitions(Integer page, Integer size,
                                                   String title, Long categoryId, Integer status) {
        IPage<Exhibition> result = exhibitionMapper.selectPageWithCategory(
                new Page<>(page, size), title, categoryId, status);
        return new PageResult<>(result.getRecords(), result.getTotal(), page, size);
    }

    @Override
    public Exhibition getById(Long id) {
        Exhibition exhibition = exhibitionMapper.selectById(id);
        if (exhibition == null) {
            throw new BusinessException("展览不存在");
        }
        exhibitionMapper.update(null,
                new LambdaUpdateWrapper<Exhibition>()
                        .eq(Exhibition::getId, id)
                        .setSql("view_count = view_count + 1"));
        exhibition.setViewCount(exhibition.getViewCount() + 1);
        return exhibition;
    }

    @Override
    public Exhibition getDetailById(Long id) {
        Exhibition exhibition = exhibitionMapper.selectById(id);
        if (exhibition == null) {
            throw new BusinessException("展览不存在");
        }
        return exhibition;
    }

    @Override
    public void create(ExhibitionDTO dto) {
        Exhibition exhibition = new Exhibition();
        exhibition.setTitle(dto.getTitle());
        exhibition.setDescription(dto.getDescription());
        exhibition.setCoverImage(dto.getCoverImage());
        exhibition.setImages(dto.getImages());
        exhibition.setCategoryId(dto.getCategoryId());
        exhibition.setLocation(dto.getLocation());
        exhibition.setHallName(dto.getHallName());
        exhibition.setStartDate(dto.getStartDate());
        exhibition.setEndDate(dto.getEndDate());
        exhibition.setDailyCapacity(dto.getDailyCapacity());
        exhibition.setTicketPrice(dto.getTicketPrice());
        exhibition.setStatus(dto.getStatus() != null ? dto.getStatus() : 0);
        exhibition.setViewCount(0);
        exhibitionMapper.insert(exhibition);
    }

    @Override
    public void update(Long id, ExhibitionDTO dto) {
        Exhibition exhibition = exhibitionMapper.selectById(id);
        if (exhibition == null) {
            throw new BusinessException("展览不存在");
        }
        exhibition.setTitle(dto.getTitle());
        exhibition.setDescription(dto.getDescription());
        exhibition.setCoverImage(dto.getCoverImage());
        exhibition.setImages(dto.getImages());
        exhibition.setCategoryId(dto.getCategoryId());
        exhibition.setLocation(dto.getLocation());
        exhibition.setHallName(dto.getHallName());
        exhibition.setStartDate(dto.getStartDate());
        exhibition.setEndDate(dto.getEndDate());
        exhibition.setDailyCapacity(dto.getDailyCapacity());
        exhibition.setTicketPrice(dto.getTicketPrice());
        exhibition.setStatus(dto.getStatus());
        exhibitionMapper.updateById(exhibition);
    }

    @Override
    public void delete(Long id) {
        exhibitionMapper.deleteById(id);
    }
}
