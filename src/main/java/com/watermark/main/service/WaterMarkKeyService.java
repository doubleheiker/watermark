package com.watermark.main.service;

import com.watermark.main.entity.UserInfo;
import com.watermark.main.entity.WaterMarkKey;

import java.util.List;

public interface WaterMarkKeyService {
    List<WaterMarkKey> getUserKey(UserInfo userInfo);
}
