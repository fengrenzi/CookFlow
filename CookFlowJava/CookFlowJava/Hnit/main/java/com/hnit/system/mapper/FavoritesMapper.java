package com.hnit.system.mapper;

import java.util.List;
import com.hnit.system.domain.Favorites;

/**
 * 用户收藏Mapper接口
 * 
 * @author Z
 * @date 2026-03-23
 */
public interface FavoritesMapper 
{
    /**
     * 查询用户收藏
     * 
     * @param userId 用户收藏主键
     * @return 用户收藏
     */
    public Favorites selectFavoritesByUserId(Long userId);

    /**
     * 查询用户收藏列表
     * 
     * @param favorites 用户收藏
     * @return 用户收藏集合
     */
    public List<Favorites> selectFavoritesList(Favorites favorites);

    /**
     * 新增用户收藏
     * 
     * @param favorites 用户收藏
     * @return 结果
     */
    public int insertFavorites(Favorites favorites);

    /**
     * 修改用户收藏
     * 
     * @param favorites 用户收藏
     * @return 结果
     */
    public int updateFavorites(Favorites favorites);

    /**
     * 删除用户收藏
     * 
     * @param userId 用户收藏主键
     * @return 结果
     */
    public int deleteFavoritesByUserId(Long userId);

    /**
     * 批量删除用户收藏
     * 
     * @param userIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFavoritesByUserIds(Long[] userIds);
}
