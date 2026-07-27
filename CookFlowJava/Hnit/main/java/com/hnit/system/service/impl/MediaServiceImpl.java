package com.hnit.system.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hnit.system.mapper.MediaMapper;
import com.hnit.system.domain.Media;
import com.hnit.system.service.IMediaService;

/**
 * 媒体资源（图片/视频/音频）Service业务层处理
 * 
 * @author Z
 * @date 2026-03-23
 */
@Service
public class MediaServiceImpl implements IMediaService 
{
    @Resource
    private MediaMapper mediaMapper;

    /**
     * 查询媒体资源（图片/视频/音频）
     * 
     * @param id 媒体资源（图片/视频/音频）主键
     * @return 媒体资源（图片/视频/音频）
     */
    @Override
    public Media selectMediaById(Long id)
    {
        return mediaMapper.selectMediaById(id);
    }

    /**
     * 查询媒体资源（图片/视频/音频）列表
     * 
     * @param media 媒体资源（图片/视频/音频）
     * @return 媒体资源（图片/视频/音频）
     */
    @Override
    public List<Media> selectMediaList(Media media)
    {
        return mediaMapper.selectMediaList(media);
    }

    /**
     * 新增媒体资源（图片/视频/音频）
     * 
     * @param media 媒体资源（图片/视频/音频）
     * @return 结果
     */
    @Override
    public int insertMedia(Media media)
    {
        return mediaMapper.insertMedia(media);
    }

    /**
     * 修改媒体资源（图片/视频/音频）
     * 
     * @param media 媒体资源（图片/视频/音频）
     * @return 结果
     */
    @Override
    public int updateMedia(Media media)
    {
        return mediaMapper.updateMedia(media);
    }

    /**
     * 批量删除媒体资源（图片/视频/音频）
     * 
     * @param ids 需要删除的媒体资源（图片/视频/音频）主键
     * @return 结果
     */
    @Override
    public int deleteMediaByIds(Long[] ids)
    {
        return mediaMapper.deleteMediaByIds(ids);
    }

    /**
     * 删除媒体资源（图片/视频/音频）信息
     * 
     * @param id 媒体资源（图片/视频/音频）主键
     * @return 结果
     */
    @Override
    public int deleteMediaById(Long id)
    {
        return mediaMapper.deleteMediaById(id);
    }
}
