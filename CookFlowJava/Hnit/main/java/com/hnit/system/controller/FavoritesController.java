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
import com.hnit.system.domain.Favorites;
import com.hnit.system.service.IFavoritesService;
import com.hnit.common.utils.poi.ExcelUtil;
import com.hnit.common.core.page.TableDataInfo;

/**
 * 用户收藏Controller
 * 
 * @author Z
 * @date 2026-03-23
 */
@RestController
@RequestMapping("/system/Favorites")
public class FavoritesController extends BaseController
{
    @Resource
    private IFavoritesService favoritesService;

    /**
     * 查询用户收藏列表
     */
    @PreAuthorize("@ss.hasPermi('system:Favorites:list')")
    @GetMapping("/list")
    public TableDataInfo list(Favorites favorites)
    {
        startPage();
        List<Favorites> list = favoritesService.selectFavoritesList(favorites);
        return getDataTable(list);
    }

    /**
     * 导出用户收藏列表
     */
    @PreAuthorize("@ss.hasPermi('system:Favorites:export')")
    @Log(title = "用户收藏", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Favorites favorites)
    {
        List<Favorites> list = favoritesService.selectFavoritesList(favorites);
        ExcelUtil<Favorites> util = new ExcelUtil<Favorites>(Favorites.class);
        util.exportExcel(response, list, "用户收藏数据");
    }

    /**
     * 获取用户收藏详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:Favorites:query')")
    @GetMapping(value = "/{userId}")
    public AjaxResult getInfo(@PathVariable("userId") Long userId)
    {
        return success(favoritesService.selectFavoritesByUserId(userId));
    }

    /**
     * 新增用户收藏
     */
    @PreAuthorize("@ss.hasPermi('system:Favorites:add')")
    @Log(title = "用户收藏", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Favorites favorites)
    {
        return toAjax(favoritesService.insertFavorites(favorites));
    }

    /**
     * 修改用户收藏
     */
    @PreAuthorize("@ss.hasPermi('system:Favorites:edit')")
    @Log(title = "用户收藏", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Favorites favorites)
    {
        return toAjax(favoritesService.updateFavorites(favorites));
    }

    /**
     * 删除用户收藏
     */
    @PreAuthorize("@ss.hasPermi('system:Favorites:remove')")
    @Log(title = "用户收藏", businessType = BusinessType.DELETE)
	@DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds)
    {
        return toAjax(favoritesService.deleteFavoritesByUserIds(userIds));
    }
}
