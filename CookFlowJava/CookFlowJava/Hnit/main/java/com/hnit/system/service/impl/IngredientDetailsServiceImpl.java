package com.hnit.system.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hnit.system.mapper.IngredientDetailsMapper;
import com.hnit.system.domain.IngredientDetails;
import com.hnit.system.service.IIngredientDetailsService;

/**
 * 食材详情静态内容Service业务层处理
 * 
 * @author hnit
 * @date 2026-03-23
 */
@Service
public class IngredientDetailsServiceImpl implements IIngredientDetailsService 
{
    @Resource
    private IngredientDetailsMapper ingredientDetailsMapper;

    /**
     * 查询食材详情静态内容
     * 
     * @param ingredientId 食材详情静态内容主键
     * @return 食材详情静态内容
     */
    @Override
    public IngredientDetails selectIngredientDetailsByIngredientId(String ingredientId)
    {
        return ingredientDetailsMapper.selectIngredientDetailsByIngredientId(ingredientId);
    }

    /**
     * 查询食材详情静态内容列表
     * 
     * @param ingredientDetails 食材详情静态内容
     * @return 食材详情静态内容
     */
    @Override
    public List<IngredientDetails> selectIngredientDetailsList(IngredientDetails ingredientDetails)
    {
        return ingredientDetailsMapper.selectIngredientDetailsList(ingredientDetails);
    }

    /**
     * 新增食材详情静态内容
     * 
     * @param ingredientDetails 食材详情静态内容
     * @return 结果
     */
    @Override
    public int insertIngredientDetails(IngredientDetails ingredientDetails)
    {
        return ingredientDetailsMapper.insertIngredientDetails(ingredientDetails);
    }

    /**
     * 修改食材详情静态内容
     * 
     * @param ingredientDetails 食材详情静态内容
     * @return 结果
     */
    @Override
    public int updateIngredientDetails(IngredientDetails ingredientDetails)
    {
        return ingredientDetailsMapper.updateIngredientDetails(ingredientDetails);
    }

    /**
     * 批量删除食材详情静态内容
     * 
     * @param ingredientIds 需要删除的食材详情静态内容主键
     * @return 结果
     */
    @Override
    public int deleteIngredientDetailsByIngredientIds(String[] ingredientIds)
    {
        return ingredientDetailsMapper.deleteIngredientDetailsByIngredientIds(ingredientIds);
    }

    /**
     * 删除食材详情静态内容信息
     * 
     * @param ingredientId 食材详情静态内容主键
     * @return 结果
     */
    @Override
    public int deleteIngredientDetailsByIngredientId(String ingredientId)
    {
        return ingredientDetailsMapper.deleteIngredientDetailsByIngredientId(ingredientId);
    }
}
