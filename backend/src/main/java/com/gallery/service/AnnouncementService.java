package com.gallery.service;

import com.gallery.common.PageResult;
import com.gallery.dto.AnnouncementDTO;
import com.gallery.entity.Announcement;

public interface AnnouncementService {

    PageResult<Announcement> list(Integer page, Integer size, Integer status);

    Announcement getById(Long id);

    void create(AnnouncementDTO dto);

    void update(Long id, AnnouncementDTO dto);

    void delete(Long id);
}
