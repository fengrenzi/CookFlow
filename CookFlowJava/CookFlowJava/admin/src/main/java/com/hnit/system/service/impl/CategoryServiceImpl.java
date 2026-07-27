package com.hnit.system.service.impl;

import com.hnit.system.domain.Category;
import com.hnit.system.domain.dto.CategoryDto;
import com.hnit.system.domain.dto.CategoryQueryDto;
import com.hnit.system.domain.dto.CategoryTreeDto;
import com.hnit.system.mapper.CategoryMapper;
import com.hnit.system.service.ICategoryService;
import com.hnit.system.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements ICategoryService {
    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> selectCategoryList(Category category) {
        return categoryMapper.selectCategoryList(category);
    }

    @Override
    public Category selectCategoryById(String id) {
        return categoryMapper.selectCategoryById(id);
    }

    @Override
    public int insertCategory(Category category) {
        if (category.getId() == null) {
            category.setId(UUID.randomUUID().toString());
        }
        return categoryMapper.insertCategory(category);
    }

    @Override
    public int updateCategory(Category category) {
        return categoryMapper.updateCategory(category);
    }

    @Override
    public int deleteCategoryByIds(String[] ids) {
        return categoryMapper.deleteCategoryByIds(ids);
    }

    @Override
    public List<CategoryTreeDto> getCategoryTree(String tableName) {
        // 保留原有实现（如果还有调用），但建议直接使用 getCategoryTreeWithImages
        return getCategoryTreeWithImages(tableName);
    }

    @Override
    public List<CategoryDto> selectHotCategoryDto(String tableName, int limit) {
        return categoryMapper.selectHotCategoryDto(tableName, limit);
    }

    @Override
    public List<CategoryDto> selectCategoryDtoList(CategoryQueryDto query) {
        return categoryMapper.selectCategoryDtoList(query, ImageUtils.getBaseUrl());
    }

    @Override
    public List<CategoryTreeDto> getCategoryTreeWithImages(String tableName) {
        // 查询该表下所有分类（含图片URL）
        List<CategoryDto> all = categoryMapper.selectAllByTableNameWithImages(tableName, ImageUtils.getBaseUrl());
        if (all == null || all.isEmpty()) {
            return Collections.emptyList();
        }

        // 构建 id -> CategoryDto 映射
        Map<String, CategoryDto> idToNode = all.stream().collect(Collectors.toMap(CategoryDto::getId, dto -> dto));

        // 找出根节点（parentId 为 null 或空字符串）
        List<CategoryTreeDto> roots = new ArrayList<>();
        for (CategoryDto dto : all) {
            String parentId = dto.getParentId();
            if (parentId == null || parentId.isEmpty()) {
                // 根节点，转换为树节点
                CategoryTreeDto root = new CategoryTreeDto();
                root.setId(dto.getId());
                root.setName(dto.getName());
                root.setImageUrl(dto.getImageUrl());
                root.setChildren(new ArrayList<>());
                roots.add(root);
            } else {
                // 非根节点，找到父节点并添加
                CategoryDto parentDto = idToNode.get(parentId);
                if (parentDto != null) {
                    // 但我们需要的是树节点，而 parentDto 是 CategoryDto，我们需要找到对应的 CategoryTreeDto
                    // 由于构建顺序未知，先收集所有节点后再挂载
                }
            }
        }

        // 更稳健的做法：先构建一个 Map<String, CategoryTreeDto>，然后递归组装
        Map<String, CategoryTreeDto> treeNodeMap = new HashMap<>();
        for (CategoryDto dto : all) {
            CategoryTreeDto node = new CategoryTreeDto();
            node.setId(dto.getId());
            node.setName(dto.getName());
            node.setImageUrl(dto.getImageUrl());
            node.setChildren(new ArrayList<>());
            treeNodeMap.put(dto.getId(), node);
        }

        List<CategoryTreeDto> result = new ArrayList<>();
        for (CategoryDto dto : all) {
            String parentId = dto.getParentId();
            CategoryTreeDto node = treeNodeMap.get(dto.getId());
            if (parentId == null || parentId.isEmpty()) {
                result.add(node);
            } else {
                CategoryTreeDto parent = treeNodeMap.get(parentId);
                if (parent != null) {
                    parent.getChildren().add(node);
                } else {
                    // 父节点不存在，作为根（防止数据异常）
                    result.add(node);
                }
            }
        }

        // 对子节点进行排序（可选）
        sortTreeChildren(result);
        return result;
    }

    private void sortTreeChildren(List<CategoryTreeDto> nodes) {
        if (nodes == null) return;
        nodes.sort(Comparator.comparing(CategoryTreeDto::getName)); // 按名称排序
        for (CategoryTreeDto node : nodes) {
            if (node.getChildren() != null) {
                sortTreeChildren(node.getChildren());
            }
        }
    }

    @Override
    public List<CategoryTreeDto> getIngredientTree() {
        // 1. 查询所有一级分类（parent_id IS NULL）
        List<Category> rootCategories = categoryMapper.selectRootCategoriesByTableName("ingredients");
        if (rootCategories == null || rootCategories.isEmpty()) {
            return Collections.emptyList();
        }

        // 2. 查询所有二级分类（按 sort 排序）
        List<Category> subCategories = categoryMapper.selectSubCategoriesByTableName("ingredients");
        // 按 parentId 分组
        Map<String, List<Category>> subMap = subCategories.stream()
                .collect(Collectors.groupingBy(Category::getParentId));

        // 3. 构建树形结构
        List<CategoryTreeDto> result = new ArrayList<>();
        for (Category root : rootCategories) {
            CategoryTreeDto dto = new CategoryTreeDto();
            dto.setId(root.getId());
            dto.setName(root.getDesignation());
            if (root.getImageId() != null && !root.getImageId().isEmpty()) {
                dto.setImageUrl(ImageUtils.getBaseUrl() + root.getImageId() + ".jpg");
            }

            // 获取该一级分类下的二级食材
            List<Category> childrenList = subMap.getOrDefault(root.getId(), Collections.emptyList());
            List<CategoryTreeDto> children = childrenList.stream()
                    .map(sub -> {
                        CategoryTreeDto childDto = new CategoryTreeDto();
                        childDto.setId(sub.getId());
                        childDto.setName(sub.getDesignation());
                        if (sub.getImageId() != null && !sub.getImageId().isEmpty()) {
                            childDto.setImageUrl(ImageUtils.getBaseUrl() + sub.getImageId() + ".jpg");
                        }
                        childDto.setChildren(Collections.emptyList()); // 二级节点无子节点
                        return childDto;
                    })
                    .collect(Collectors.toList());
            dto.setChildren(children);
            result.add(dto);
        }

        return result;
    }
}