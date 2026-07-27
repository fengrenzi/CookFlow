package com.hnit.system.mapper;

import com.hnit.system.domain.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    int insert(ShoppingCart record);
    int updateQuantity(@Param("id") String id, @Param("quantity") Integer quantity);
    int deleteById(@Param("id") String id);
    ShoppingCart selectById(@Param("id") String id);
    List<ShoppingCart> selectByUserId(@Param("userId") Long userId);
    int deleteByUserId(@Param("userId") Long userId);
    ShoppingCart selectByUserAndItem(@Param("userId") Long userId, @Param("itemType") String itemType, @Param("itemId") String itemId);
}