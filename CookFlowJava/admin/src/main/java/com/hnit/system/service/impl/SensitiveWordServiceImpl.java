package com.hnit.system.service.impl;

import com.hnit.system.domain.SensitiveWord;
import com.hnit.system.mapper.SensitiveWordMapper;
import com.hnit.system.service.ISensitiveWordService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

@Service
public class SensitiveWordServiceImpl implements ISensitiveWordService {

    @Resource
    private SensitiveWordMapper sensitiveWordMapper;

    private volatile Map<String, String> sensitiveMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        refreshCache();
    }

    @Override
    public void refreshCache() {
        List<SensitiveWord> list = selectList(null);
        Map<String, String> newMap = new ConcurrentHashMap<>();
        for (SensitiveWord word : list) {
            newMap.put(word.getWord(), word.getReplaceWith());
        }
        sensitiveMap = newMap;
    }

    @Override
    public List<SensitiveWord> selectList(SensitiveWord word) {
        return sensitiveWordMapper.selectList(word);
    }

    @Override
    public boolean save(SensitiveWord word) {
        int rows = sensitiveWordMapper.insert(word);
        if (rows > 0) refreshCache();
        return rows > 0;
    }

    @Override
    public boolean updateById(SensitiveWord word) {
        int rows = sensitiveWordMapper.updateById(word);
        if (rows > 0) refreshCache();
        return rows > 0;
    }

    @Override
    public boolean removeByIds(List<String> ids) {
        int rows = 0;
        for (String id : ids) {
            rows += sensitiveWordMapper.deleteById(id);
        }
        if (rows > 0) refreshCache();
        return rows > 0;
    }

    @Override
    public String filterText(String text) {
        if (text == null || text.isEmpty()) return text;
        String result = text;
        for (Map.Entry<String, String> entry : sensitiveMap.entrySet()) {
            result = Pattern.compile(Pattern.quote(entry.getKey()), Pattern.CASE_INSENSITIVE)
                    .matcher(result).replaceAll(entry.getValue());
        }
        return result;
    }
}