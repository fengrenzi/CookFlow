package com.hnit.system.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ImageUtils {
    private static String baseUrl;

    @Value("${img.base-url}")
    public void setBaseUrl(String baseUrl) {
        ImageUtils.baseUrl = baseUrl;
    }

    /**
     * 获取图片访问基础 URL
     */
    public static String getBaseUrl() {
        return baseUrl;
    }

    /**
     * 根据图片 ID 获取完整访问 URL
     */
    public static String getFullUrl(String imageId) {
        if (imageId == null || imageId.isEmpty()) {
            return null;
        }
        return baseUrl + imageId + ".jpg";
    }
}