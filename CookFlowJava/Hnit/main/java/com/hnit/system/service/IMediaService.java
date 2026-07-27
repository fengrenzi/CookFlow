package com.hnit.system.service;

import java.util.List;
import com.hnit.system.domain.Media;

/**
 * 媒体资源（图片/视频/音频）Service接口
 * 
 * @author Z
 * @date 2026-03-23
 */
public interface IMediaService 
{
    /**
     * 查询媒体资源（图片/视频/音频）
     * 
     * @param id 媒体资源（图片/视频/音频）主键
     * @return 媒体资源（图片/视频/音频）
     */
    public Media selectMediaById(Long id);

    /**
     * 查询媒体资源（图片/视频/音频）列表
     * 
     * @param media 媒体资源（图片/视频/音频）
     * @return 媒体资源（图片/视频/音频）集合
     */
    public List<Media> selectMediaList(Media media);

    /**
     * 新增媒体资源（图片/视频/音频）
     * 
     * @param media 媒体资源（图片/视频/音频）
     * @return 结果
     */
    public int insertMedia(Media media);

    /**
     * 修改媒体资源（图片/视频/音频）
     * 
     * @param media 媒体资源（图片/视频/音频）
     * @return 结果
     */
    public int updateMedia(Media media);

    /**
     * 批量删除媒体资源（图片/视频/音频）
     * 
     * @param ids 需要删除的媒体资源（图片/视频/音频）主键集合
     * @return 结果
     */
    public int deleteMediaByIds(Long[] ids);

    /**
     * 删除媒体资源（图片/视频/音频）信息
     * 
     * @param id 媒体资源（图片/视频/音频）主键
     * @return 结果
     */
    public int deleteMediaById(Long id);
}
