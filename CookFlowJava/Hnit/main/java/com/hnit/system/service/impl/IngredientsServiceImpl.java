package com.hnit.system.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hnit.system.mapper.IngredientsMapper;
import com.hnit.system.domain.Ingredients;
import com.hnit.system.service.IIngredientsService;

/**
 * 食材Service业务层处理
 * 
 * @author Z
 * @date 2026-03-23
 */
@Service
public class IngredientsServiceImpl implements IIngredientsService 
{
    @Resource
    private IngredientsMapper ingredientsMapper;

    /**
     * 查询食材
     * 
     * @param id 食材主键
     * @return 食材
     */
    @Override
    public Ingredients selectIngredientsById(String id)
    {
        return ingredientsMapper.selectIngredientsById(id);
    }

    /**
     * 查询食材列表
     * 
     * @param ingredients 食材
     * @return 食材
     */
    @Override
    public List<Ingredients> selectIngredientsList(Ingredients ingredients)
    {
        return ingredientsMapper.selectIngredientsList(ingredients);
    }

    /**
     * 新增食材
     * 
     * @param ingredients 食材
     * @return 结果
     */
    @Override
    public int insertIngredients(Ingredients ingredients)
    {
        return ingredientsMapper.insertIngredients(ingredients);
    }

    /**
     * 修改食材
     * 
     * @param ingredients 食材
     * @return 结果
     */
    @Override
    public int updateIngredients(Ingredients ingredients)
    {
        return ingredientsMapper.updateIngredients(ingredients);
    }

    /**
     * 批量删除食材
     * 
     * @param ids 需要删除的食材主键
     * @return 结果
     */
    @Override
    public int deleteIngredientsByIds(String[] ids)
    {
        return ingredientsMapper.deleteIngredientsByIds(ids);
    }

    /**
     * 删除食材信息
     * 
     * @param id 食材主键
     * @return 结果
     */
    @Override
    public int deleteIngredientsById(String id)
    {
        return ingredientsMapper.deleteIngredientsById(id);
    }
}
