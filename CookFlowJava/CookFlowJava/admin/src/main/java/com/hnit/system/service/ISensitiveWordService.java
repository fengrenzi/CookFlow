package com.hnit.system.service;

import com.hnit.system.domain.SensitiveWord;
import java.util.List;

public interface ISensitiveWordService {
    List<SensitiveWord> selectList(SensitiveWord word);
    boolean save(SensitiveWord word);
    boolean updateById(SensitiveWord word);
    boolean removeByIds(List<String> ids);
    String filterText(String text);
    void refreshCache();
}