package com.hnit.system.mapper;

import java.util.List;
import com.hnit.system.domain.CartItems;

/**
 * 购物车项Mapper接口
 * 
 * @author Z
 * @date 2026-03-23
 */
public interface CartItemsMapper 
{
    /**
     * 查询购物车项
     * 
     * @param id 购物车项主键
     * @return 购物车项
     */
    public CartItems selectCartItemsById(Long id);

    /**
     * 查询购物车项列表
     * 
     * @param cartItems 购物车项
     * @return 购物车项集合
     */
    public List<CartItems> selectCartItemsList(CartItems cartItems);

    /**
     * 新增购物车项
     * 
     * @param cartItems 购物车项
     * @return 结果
     */
    public int insertCartItems(CartItems cartItems);

    /**
     * 修改购物车项
     * 
     * @param cartItems 购物车项
     * @return 结果
     */
    public int updateCartItems(CartItems cartItems);

    /**
     * 删除购物车项
     * 
     * @param id 购物车项主键
     * @return 结果
     */
    public int deleteCartItemsById(Long id);

    /**
     * 批量删除购物车项
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCartItemsByIds(Long[] ids);
}
