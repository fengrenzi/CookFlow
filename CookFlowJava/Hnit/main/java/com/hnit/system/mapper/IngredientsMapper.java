package com.hnit.system.mapper;

import java.util.List;
import com.hnit.system.domain.Ingredients;

/**
 * 食材Mapper接口
 * 
 * @author Z
 * @date 2026-03-23
 */
public interface IngredientsMapper 
{
    /**
     * 查询食材
     * 
     * @param id 食材主键
     * @return 食材
     */
    public Ingredients selectIngredientsById(String id);

    /**
     * 查询食材列表
     * 
     * @param ingredients 食材
     * @return 食材集合
     */
    public List<Ingredients> selectIngredientsList(Ingredients ingredients);

    /**
     * 新增食材
     * 
     * @param ingredients 食材
     * @return 结果
     */
    public int insertIngredients(Ingredients ingredients);

    /**
     * 修改食材
     * 
     * @param ingredients 食材
     * @return 结果
     */
    public int updateIngredients(Ingredients ingredients);

    /**
     * 删除食材
     * 
     * @param id 食材主键
     * @return 结果
     */
    public int deleteIngredientsById(String id);

    /**
     * 批量删除食材
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteIngredientsByIds(String[] ids);
}
