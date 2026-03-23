package com.gallery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gallery.common.BusinessException;
import com.gallery.common.PageResult;
import com.gallery.dto.ExhibitItemDTO;
import com.gallery.entity.ExhibitItem;
import com.gallery.mapper.ExhibitItemMapper;
import com.gallery.service.ExhibitItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExhibitItemServiceImpl implements ExhibitItemService {

    @Autowired
    private ExhibitItemMapper exhibitItemMapper;

    @Override
    public PageResult<ExhibitItem> list(Integer page, Integer size, Long exhibitionId) {
        LambdaQueryWrapper<ExhibitItem> wrapper = new LambdaQueryWrapper<>();
        if (exhibitionId != null) {
            wrapper.eq(ExhibitItem::getExhibitionId, exhibitionId);
        }
        wrapper.orderByAsc(ExhibitItem::getSortOrder);

        IPage<ExhibitItem> result = exhibitItemMapper.selectPage(new Page<>(page, size), wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), page, size);
    }

    @Override
    public List<ExhibitItem> listByExhibitionId(Long exhibitionId) {
        return exhibitItemMapper.selectList(
                new LambdaQueryWrapper<ExhibitItem>()
                        .eq(ExhibitItem::getExhibitionId, exhibitionId)
                        .orderByAsc(ExhibitItem::getSortOrder));
    }

    @Override
    public ExhibitItem getById(Long id) {
        ExhibitItem item = exhibitItemMapper.selectById(id);
        if (item == null) {
            throw new BusinessException("展品不存在");
        }
        return item;
    }

    @Override
    public void create(ExhibitItemDTO dto) {
        ExhibitItem item = new ExhibitItem();
        item.setExhibitionId(dto.getExhibitionId());
        item.setName(dto.getName());
        item.setArtist(dto.getArtist());
        item.setEra(dto.getEra());
        item.setDescription(dto.getDescription());
        item.setCoverImage(dto.getCoverImage());
        item.setAudioUrl(dto.getAudioUrl());
        item.setSortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : 0);
        exhibitItemMapper.insert(item);
    }

    @Override
    public void update(Long id, ExhibitItemDTO dto) {
        ExhibitItem item = exhibitItemMapper.selectById(id);
        if (item == null) {
            throw new BusinessException("展品不存在");
        }
        item.setExhibitionId(dto.getExhibitionId());
        item.setName(dto.getName());
        item.setArtist(dto.getArtist());
        item.setEra(dto.getEra());
        item.setDescription(dto.getDescription());
        item.setCoverImage(dto.getCoverImage());
        item.setAudioUrl(dto.getAudioUrl());
        item.setSortOrder(dto.getSortOrder());
        exhibitItemMapper.updateById(item);
    }

    @Override
    public void delete(Long id) {
        exhibitItemMapper.deleteById(id);
    }
}
