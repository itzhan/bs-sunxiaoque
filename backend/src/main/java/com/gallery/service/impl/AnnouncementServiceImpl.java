package com.gallery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gallery.common.BusinessException;
import com.gallery.common.PageResult;
import com.gallery.dto.AnnouncementDTO;
import com.gallery.entity.Announcement;
import com.gallery.mapper.AnnouncementMapper;
import com.gallery.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {

    @Autowired
    private AnnouncementMapper announcementMapper;

    @Override
    public PageResult<Announcement> list(Integer page, Integer size, Integer status) {
        LambdaQueryWrapper<Announcement> wrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            wrapper.eq(Announcement::getStatus, status);
        }
        wrapper.orderByDesc(Announcement::getTopFlag)
               .orderByDesc(Announcement::getCreateTime);

        IPage<Announcement> result = announcementMapper.selectPage(new Page<>(page, size), wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), page, size);
    }

    @Override
    public Announcement getById(Long id) {
        Announcement announcement = announcementMapper.selectById(id);
        if (announcement == null) {
            throw new BusinessException("公告不存在");
        }
        return announcement;
    }

    @Override
    public void create(AnnouncementDTO dto) {
        Announcement announcement = new Announcement();
        announcement.setTitle(dto.getTitle());
        announcement.setContent(dto.getContent());
        announcement.setCoverImage(dto.getCoverImage());
        announcement.setStatus(dto.getStatus() != null ? dto.getStatus() : 0);
        announcement.setTopFlag(dto.getTopFlag() != null ? dto.getTopFlag() : 0);
        announcementMapper.insert(announcement);
    }

    @Override
    public void update(Long id, AnnouncementDTO dto) {
        Announcement announcement = announcementMapper.selectById(id);
        if (announcement == null) {
            throw new BusinessException("公告不存在");
        }
        announcement.setTitle(dto.getTitle());
        announcement.setContent(dto.getContent());
        announcement.setCoverImage(dto.getCoverImage());
        announcement.setStatus(dto.getStatus());
        announcement.setTopFlag(dto.getTopFlag());
        announcementMapper.updateById(announcement);
    }

    @Override
    public void delete(Long id) {
        announcementMapper.deleteById(id);
    }
}
