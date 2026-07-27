package com.hnit.system.service;

import com.hnit.system.domain.dto.AddToCartDTO;
import com.hnit.system.domain.dto.UpdateCartQuantityDTO;
import com.hnit.system.domain.vo.ShoppingCartItemVO;
import com.hnit.system.domain.vo.ShoppingListTextVO;
import java.util.List;

public interface IShoppingCartService {
    void addToCart(Long userId, AddToCartDTO dto);
    void updateQuantity(Long userId, String cartId, UpdateCartQuantityDTO dto);
    void removeFromCart(Long userId, String cartId);
    List<ShoppingCartItemVO> getCartList(Long userId);
    ShoppingListTextVO generateShoppingListText(Long userId, List<String> selectedCartIds);
}