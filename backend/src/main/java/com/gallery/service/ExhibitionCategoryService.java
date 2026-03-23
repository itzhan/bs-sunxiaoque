package com.gallery.service;

import com.gallery.common.PageResult;
import com.gallery.dto.CategoryDTO;
import com.gallery.entity.ExhibitionCategory;

import java.util.List;

public interface ExhibitionCategoryService {

    PageResult<ExhibitionCategory> list(Integer page, Integer size, String keyword, Integer status);

    List<ExhibitionCategory> listAll();

    ExhibitionCategory getById(Long id);

    void create(CategoryDTO dto);

    void update(Long id, CategoryDTO dto);

    void delete(Long id);
}
