package com.hnit.system.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hnit.system.mapper.FavoritesMapper;
import com.hnit.system.domain.Favorites;
import com.hnit.system.service.IFavoritesService;

/**
 * 用户收藏Service业务层处理
 * 
 * @author Z
 * @date 2026-03-23
 */
@Service
public class FavoritesServiceImpl implements IFavoritesService 
{
    @Resource
    private FavoritesMapper favoritesMapper;

    /**
     * 查询用户收藏
     * 
     * @param userId 用户收藏主键
     * @return 用户收藏
     */
    @Override
    public Favorites selectFavoritesByUserId(Long userId)
    {
        return favoritesMapper.selectFavoritesByUserId(userId);
    }

    /**
     * 查询用户收藏列表
     * 
     * @param favorites 用户收藏
     * @return 用户收藏
     */
    @Override
    public List<Favorites> selectFavoritesList(Favorites favorites)
    {
        return favoritesMapper.selectFavoritesList(favorites);
    }

    /**
     * 新增用户收藏
     * 
     * @param favorites 用户收藏
     * @return 结果
     */
    @Override
    public int insertFavorites(Favorites favorites)
    {
        return favoritesMapper.insertFavorites(favorites);
    }

    /**
     * 修改用户收藏
     * 
     * @param favorites 用户收藏
     * @return 结果
     */
    @Override
    public int updateFavorites(Favorites favorites)
    {
        return favoritesMapper.updateFavorites(favorites);
    }

    /**
     * 批量删除用户收藏
     * 
     * @param userIds 需要删除的用户收藏主键
     * @return 结果
     */
    @Override
    public int deleteFavoritesByUserIds(Long[] userIds)
    {
        return favoritesMapper.deleteFavoritesByUserIds(userIds);
    }

    /**
     * 删除用户收藏信息
     * 
     * @param userId 用户收藏主键
     * @return 结果
     */
    @Override
    public int deleteFavoritesByUserId(Long userId)
    {
        return favoritesMapper.deleteFavoritesByUserId(userId);
    }
}
