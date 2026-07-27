package com.hnit.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hnit.common.annotation.Log;
import com.hnit.common.core.controller.BaseController;
import com.hnit.common.core.domain.AjaxResult;
import com.hnit.common.enums.BusinessType;
import com.hnit.system.domain.CartItems;
import com.hnit.system.service.ICartItemsService;
import com.hnit.common.utils.poi.ExcelUtil;
import com.hnit.common.core.page.TableDataInfo;

/**
 * 购物车项Controller
 * 
 * @author Z
 * @date 2026-03-23
 */
@RestController
@RequestMapping("/system/CartItems")
public class CartItemsController extends BaseController
{
    @Resource
    private ICartItemsService cartItemsService;

    /**
     * 查询购物车项列表
     */
    @PreAuthorize("@ss.hasPermi('system:CartItems:list')")
    @GetMapping("/list")
    public TableDataInfo list(CartItems cartItems)
    {
        startPage();
        List<CartItems> list = cartItemsService.selectCartItemsList(cartItems);
        return getDataTable(list);
    }

    /**
     * 导出购物车项列表
     */
    @PreAuthorize("@ss.hasPermi('system:CartItems:export')")
    @Log(title = "购物车项", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CartItems cartItems)
    {
        List<CartItems> list = cartItemsService.selectCartItemsList(cartItems);
        ExcelUtil<CartItems> util = new ExcelUtil<CartItems>(CartItems.class);
        util.exportExcel(response, list, "购物车项数据");
    }

    /**
     * 获取购物车项详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:CartItems:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(cartItemsService.selectCartItemsById(id));
    }

    /**
     * 新增购物车项
     */
    @PreAuthorize("@ss.hasPermi('system:CartItems:add')")
    @Log(title = "购物车项", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CartItems cartItems)
    {
        return toAjax(cartItemsService.insertCartItems(cartItems));
    }

    /**
     * 修改购物车项
     */
    @PreAuthorize("@ss.hasPermi('system:CartItems:edit')")
    @Log(title = "购物车项", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CartItems cartItems)
    {
        return toAjax(cartItemsService.updateCartItems(cartItems));
    }

    /**
     * 删除购物车项
     */
    @PreAuthorize("@ss.hasPermi('system:CartItems:remove')")
    @Log(title = "购物车项", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(cartItemsService.deleteCartItemsByIds(ids));
    }
}
