package com.hnit.system.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hnit.system.mapper.SensitiveWordsMapper;
import com.hnit.system.domain.SensitiveWords;
import com.hnit.system.service.ISensitiveWordsService;

/**
 * 敏感词库，用于内容审核/替换Service业务层处理
 * 
 * @author Z
 * @date 2026-03-23
 */
@Service
public class SensitiveWordsServiceImpl implements ISensitiveWordsService 
{
    @Resource
    private SensitiveWordsMapper sensitiveWordsMapper;

    /**
     * 查询敏感词库，用于内容审核/替换
     * 
     * @param id 敏感词库，用于内容审核/替换主键
     * @return 敏感词库，用于内容审核/替换
     */
    @Override
    public SensitiveWords selectSensitiveWordsById(Long id)
    {
        return sensitiveWordsMapper.selectSensitiveWordsById(id);
    }

    /**
     * 查询敏感词库，用于内容审核/替换列表
     * 
     * @param sensitiveWords 敏感词库，用于内容审核/替换
     * @return 敏感词库，用于内容审核/替换
     */
    @Override
    public List<SensitiveWords> selectSensitiveWordsList(SensitiveWords sensitiveWords)
    {
        return sensitiveWordsMapper.selectSensitiveWordsList(sensitiveWords);
    }

    /**
     * 新增敏感词库，用于内容审核/替换
     * 
     * @param sensitiveWords 敏感词库，用于内容审核/替换
     * @return 结果
     */
    @Override
    public int insertSensitiveWords(SensitiveWords sensitiveWords)
    {
        return sensitiveWordsMapper.insertSensitiveWords(sensitiveWords);
    }

    /**
     * 修改敏感词库，用于内容审核/替换
     * 
     * @param sensitiveWords 敏感词库，用于内容审核/替换
     * @return 结果
     */
    @Override
    public int updateSensitiveWords(SensitiveWords sensitiveWords)
    {
        return sensitiveWordsMapper.updateSensitiveWords(sensitiveWords);
    }

    /**
     * 批量删除敏感词库，用于内容审核/替换
     * 
     * @param ids 需要删除的敏感词库，用于内容审核/替换主键
     * @return 结果
     */
    @Override
    public int deleteSensitiveWordsByIds(Long[] ids)
    {
        return sensitiveWordsMapper.deleteSensitiveWordsByIds(ids);
    }

    /**
     * 删除敏感词库，用于内容审核/替换信息
     * 
     * @param id 敏感词库，用于内容审核/替换主键
     * @return 结果
     */
    @Override
    public int deleteSensitiveWordsById(Long id)
    {
        return sensitiveWordsMapper.deleteSensitiveWordsById(id);
    }
}
