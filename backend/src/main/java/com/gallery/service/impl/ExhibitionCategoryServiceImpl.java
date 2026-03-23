package com.gallery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gallery.common.BusinessException;
import com.gallery.common.PageResult;
import com.gallery.dto.CategoryDTO;
import com.gallery.entity.ExhibitionCategory;
import com.gallery.mapper.ExhibitionCategoryMapper;
import com.gallery.service.ExhibitionCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExhibitionCategoryServiceImpl implements ExhibitionCategoryService {

    @Autowired
    private ExhibitionCategoryMapper categoryMapper;

    @Override
    public PageResult<ExhibitionCategory> list(Integer page, Integer size, String keyword, Integer status) {
        LambdaQueryWrapper<ExhibitionCategory> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(ExhibitionCategory::getName, keyword);
        }
        if (status != null) {
            wrapper.eq(ExhibitionCategory::getStatus, status);
        }
        wrapper.orderByAsc(ExhibitionCategory::getSortOrder);

        IPage<ExhibitionCategory> result = categoryMapper.selectPage(new Page<>(page, size), wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), page, size);
    }

    @Override
    public List<ExhibitionCategory> listAll() {
        return categoryMapper.selectList(
                new LambdaQueryWrapper<ExhibitionCategory>()
                        .eq(ExhibitionCategory::getStatus, 1)
                        .orderByAsc(ExhibitionCategory::getSortOrder));
    }

    @Override
    public ExhibitionCategory getById(Long id) {
        ExhibitionCategory category = categoryMapper.selectById(id);
        if (category == null) {
            throw new BusinessException("分类不存在");
        }
        return category;
    }

    @Override
    public void create(CategoryDTO dto) {
        ExhibitionCategory category = new ExhibitionCategory();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setSortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : 0);
        category.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        categoryMapper.insert(category);
    }

    @Override
    public void update(Long id, CategoryDTO dto) {
        ExhibitionCategory category = categoryMapper.selectById(id);
        if (category == null) {
            throw new BusinessException("分类不存在");
        }
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setSortOrder(dto.getSortOrder());
        category.setStatus(dto.getStatus());
        categoryMapper.updateById(category);
    }

    @Override
    public void delete(Long id) {
        categoryMapper.deleteById(id);
    }
}
