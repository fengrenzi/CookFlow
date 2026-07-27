package com.hnit.system.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/voice")
public class VoiceController {

    @Value("${dashscope.api-key}")
    private String apiKey;

    @Value("${dashscope.asr-model:paraformer-v2}")
    private String asrModel;

    private RestTemplate restTemplate = new RestTemplate();
    private ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/recognize")
    public Map<String, String> recognize(@RequestParam("audio") MultipartFile audio) {
        Map<String, String> result = new HashMap<>();
        try {
            // 1. 获取音频字节数组（前端已转为 WAV 16kHz 单声道）
            byte[] audioData = audio.getBytes();

            // 2. 构造请求 URL，将 model 作为查询参数
            String url = "https://dashscope.aliyuncs.com/api/v1/services/audio/asr/transcription?model=" + asrModel;

            // 3. 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("audio/wav"));
            headers.set("Authorization", "Bearer " + apiKey);
            headers.set("X-DashScope-Async", "disable");   // 同步调用

            // 4. 发送请求，请求体为音频二进制
            HttpEntity<byte[]> requestEntity = new HttpEntity<>(audioData, headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            // 5. 解析 JSON 响应，提取识别文本
            String responseBody = response.getBody();
            JsonNode root = objectMapper.readTree(responseBody);
            String text = root.path("output").path("text").asText();

            result.put("success", "true");
            result.put("text", text != null ? text : "");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", "false");
            result.put("error", e.getMessage());
        }
        return result;
    }
}