package com.hnit.system.mapper;

import com.hnit.system.domain.UserInteraction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserInteractionMapper {

    /**
     * 插入交互记录
     */
    int insert(UserInteraction interaction);

    /**
     * 删除指定交互记录
     */
    int delete(@Param("userId") Long userId,
               @Param("targetType") String targetType,
               @Param("targetId") String targetId,
               @Param("interactionType") String interactionType);

    /**
     * 检查是否存在交互记录
     */
    boolean exists(@Param("userId") Long userId,
                   @Param("targetType") String targetType,
                   @Param("targetId") String targetId,
                   @Param("interactionType") String interactionType);

    // 根据用户、目标类型、交互类型查询
    List<UserInteraction> selectByUserAndType(@Param("userId") Long userId,
                                              @Param("targetType") String targetType,
                                              @Param("interactionType") String interactionType);
}