package com.hnit.system.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hnit.system.mapper.CartItemsMapper;
import com.hnit.system.domain.CartItems;
import com.hnit.system.service.ICartItemsService;

/**
 * 购物车项Service业务层处理
 * 
 * @author Z
 * @date 2026-03-23
 */
@Service
public class CartItemsServiceImpl implements ICartItemsService 
{
    @Resource
    private CartItemsMapper cartItemsMapper;

    /**
     * 查询购物车项
     * 
     * @param id 购物车项主键
     * @return 购物车项
     */
    @Override
    public CartItems selectCartItemsById(Long id)
    {
        return cartItemsMapper.selectCartItemsById(id);
    }

    /**
     * 查询购物车项列表
     * 
     * @param cartItems 购物车项
     * @return 购物车项
     */
    @Override
    public List<CartItems> selectCartItemsList(CartItems cartItems)
    {
        return cartItemsMapper.selectCartItemsList(cartItems);
    }

    /**
     * 新增购物车项
     * 
     * @param cartItems 购物车项
     * @return 结果
     */
    @Override
    public int insertCartItems(CartItems cartItems)
    {
        return cartItemsMapper.insertCartItems(cartItems);
    }

    /**
     * 修改购物车项
     * 
     * @param cartItems 购物车项
     * @return 结果
     */
    @Override
    public int updateCartItems(CartItems cartItems)
    {
        return cartItemsMapper.updateCartItems(cartItems);
    }

    /**
     * 批量删除购物车项
     * 
     * @param ids 需要删除的购物车项主键
     * @return 结果
     */
    @Override
    public int deleteCartItemsByIds(Long[] ids)
    {
        return cartItemsMapper.deleteCartItemsByIds(ids);
    }

    /**
     * 删除购物车项信息
     * 
     * @param id 购物车项主键
     * @return 结果
     */
    @Override
    public int deleteCartItemsById(Long id)
    {
        return cartItemsMapper.deleteCartItemsById(id);
    }
}
