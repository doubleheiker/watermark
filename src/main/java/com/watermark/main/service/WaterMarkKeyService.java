package com.watermark.main.service;

import com.watermark.main.entity.UserInfo;
import com.watermark.main.entity.WaterMarkKey;
import org.springframework.data.domain.Page;

import java.util.List;

public interface WaterMarkKeyService {
    Page<WaterMarkKey> getUserKeyList(UserInfo userInfo, int pageNum, int pageSize);
    List<WaterMarkKey> getUserKey(UserInfo userInfo);
    void delete(Long kid);
}
