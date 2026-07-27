package com.hnit.system.controller;

import com.hnit.common.core.controller.BaseController;
import com.hnit.common.core.domain.AjaxResult;
import com.hnit.system.domain.dto.AddToCartDTO;
import com.hnit.system.domain.dto.UpdateCartQuantityDTO;
import com.hnit.system.domain.vo.ShoppingCartItemVO;
import com.hnit.system.domain.vo.ShoppingListTextVO;
import com.hnit.system.service.IShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class ShoppingCartController extends BaseController {

    @Resource
    private IShoppingCartService cartService;

    // 临时固定用户ID，后续从token中解析
    private static final Long TEMP_USER_ID = 1L;

    @PostMapping("/add")
    public AjaxResult addToCart(@Valid @RequestBody AddToCartDTO dto) {
        cartService.addToCart(TEMP_USER_ID, dto);
        return success();
    }

    @PutMapping("/{cartId}")
    public AjaxResult updateQuantity(@PathVariable String cartId, @Valid @RequestBody UpdateCartQuantityDTO dto) {
        cartService.updateQuantity(TEMP_USER_ID, cartId, dto);
        return success();
    }

    @DeleteMapping("/{cartId}")
    public AjaxResult removeFromCart(@PathVariable String cartId) {
        cartService.removeFromCart(TEMP_USER_ID, cartId);
        return success();
    }

    @GetMapping("/list")
    public AjaxResult list() {
        List<ShoppingCartItemVO> list = cartService.getCartList(TEMP_USER_ID);
        return success(list);
    }

    @PostMapping("/generate-text")
    public AjaxResult generateText(@RequestBody(required = false) List<String> selectedCartIds) {
        ShoppingListTextVO vo = cartService.generateShoppingListText(TEMP_USER_ID, selectedCartIds);
        return success(vo);
    }
}