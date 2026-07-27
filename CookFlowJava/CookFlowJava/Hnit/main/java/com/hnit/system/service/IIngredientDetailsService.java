package com.hnit.system.service;

import java.util.List;
import com.hnit.system.domain.IngredientDetails;

/**
 * 食材详情静态内容Service接口
 * 
 * @author hnit
 * @date 2026-03-23
 */
public interface IIngredientDetailsService 
{
    /**
     * 查询食材详情静态内容
     * 
     * @param ingredientId 食材详情静态内容主键
     * @return 食材详情静态内容
     */
    public IngredientDetails selectIngredientDetailsByIngredientId(String ingredientId);

    /**
     * 查询食材详情静态内容列表
     * 
     * @param ingredientDetails 食材详情静态内容
     * @return 食材详情静态内容集合
     */
    public List<IngredientDetails> selectIngredientDetailsList(IngredientDetails ingredientDetails);

    /**
     * 新增食材详情静态内容
     * 
     * @param ingredientDetails 食材详情静态内容
     * @return 结果
     */
    public int insertIngredientDetails(IngredientDetails ingredientDetails);

    /**
     * 修改食材详情静态内容
     * 
     * @param ingredientDetails 食材详情静态内容
     * @return 结果
     */
    public int updateIngredientDetails(IngredientDetails ingredientDetails);

    /**
     * 批量删除食材详情静态内容
     * 
     * @param ingredientIds 需要删除的食材详情静态内容主键集合
     * @return 结果
     */
    public int deleteIngredientDetailsByIngredientIds(String[] ingredientIds);

    /**
     * 删除食材详情静态内容信息
     * 
     * @param ingredientId 食材详情静态内容主键
     * @return 结果
     */
    public int deleteIngredientDetailsByIngredientId(String ingredientId);
}
