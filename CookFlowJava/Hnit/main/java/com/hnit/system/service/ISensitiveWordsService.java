package com.hnit.system.service;

import java.util.List;
import com.hnit.system.domain.SensitiveWords;

/**
 * 敏感词库，用于内容审核/替换Service接口
 * 
 * @author Z
 * @date 2026-03-23
 */
public interface ISensitiveWordsService 
{
    /**
     * 查询敏感词库，用于内容审核/替换
     * 
     * @param id 敏感词库，用于内容审核/替换主键
     * @return 敏感词库，用于内容审核/替换
     */
    public SensitiveWords selectSensitiveWordsById(Long id);

    /**
     * 查询敏感词库，用于内容审核/替换列表
     * 
     * @param sensitiveWords 敏感词库，用于内容审核/替换
     * @return 敏感词库，用于内容审核/替换集合
     */
    public List<SensitiveWords> selectSensitiveWordsList(SensitiveWords sensitiveWords);

    /**
     * 新增敏感词库，用于内容审核/替换
     * 
     * @param sensitiveWords 敏感词库，用于内容审核/替换
     * @return 结果
     */
    public int insertSensitiveWords(SensitiveWords sensitiveWords);

    /**
     * 修改敏感词库，用于内容审核/替换
     * 
     * @param sensitiveWords 敏感词库，用于内容审核/替换
     * @return 结果
     */
    public int updateSensitiveWords(SensitiveWords sensitiveWords);

    /**
     * 批量删除敏感词库，用于内容审核/替换
     * 
     * @param ids 需要删除的敏感词库，用于内容审核/替换主键集合
     * @return 结果
     */
    public int deleteSensitiveWordsByIds(Long[] ids);

    /**
     * 删除敏感词库，用于内容审核/替换信息
     * 
     * @param id 敏感词库，用于内容审核/替换主键
     * @return 结果
     */
    public int deleteSensitiveWordsById(Long id);
}
