package com.watermark.main.service;

import com.watermark.main.entity.UserInfo;
import com.watermark.main.entity.WaterMarkKey;
import com.watermark.main.repository.WaterMarkKeyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WaterMarkKeyServiceImpl implements WaterMarkKeyService {
    private final WaterMarkKeyRepository waterMarkKeyRepository;

    public WaterMarkKeyServiceImpl(WaterMarkKeyRepository waterMarkKeyRepository) {
        this.waterMarkKeyRepository = waterMarkKeyRepository;
    }

    @Override
    public List<WaterMarkKey> getUserKey(UserInfo userInfo) {
        return  waterMarkKeyRepository.findAllByUser(userInfo);
    }
}
