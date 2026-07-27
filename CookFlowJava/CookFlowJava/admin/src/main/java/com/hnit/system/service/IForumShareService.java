package com.hnit.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hnit.system.domain.dto.ForumShareDto;
import com.hnit.system.domain.vo.ForumShareVo;

public interface IForumShareService {
    void createShare(ForumShareDto dto, Long userId);
    Page<ForumShareVo> listShares(int page, int size, String category, String sortBy, Long currentUserId);
    ForumShareVo getShareDetail(String id, Long currentUserId);
    void toggleLike(String shareId, Long userId);
    void toggleFavorite(String shareId, Long userId);
    void deleteShare(String id, Long userId);
}