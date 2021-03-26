package com.watermark.main.service;

import com.watermark.main.entity.UserInfo;
import com.watermark.main.entity.WaterMarkKey;
import com.watermark.main.repository.WaterMarkKeyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WaterMarkKeyServiceImpl implements WaterMarkKeyService {
    private final WaterMarkKeyRepository waterMarkKeyRepository;

    public WaterMarkKeyServiceImpl(WaterMarkKeyRepository waterMarkKeyRepository) {
        this.waterMarkKeyRepository = waterMarkKeyRepository;
    }

    /*
     * @param: userInfo
     * @Description: 通过userInfo查找当前用户的所有水印密钥，并分页
     */
    @Override
    public Page<WaterMarkKey> getUserKeyList(UserInfo userInfo, int pageNum, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.ASC, "kid");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        return waterMarkKeyRepository.findAllByUser(userInfo, pageable);
    }

    /*
     * @param: userInfo
     * @Description: 通过userInfo查找当前用户的所有水印密钥
     */
    @Override
    public List<WaterMarkKey> getUserKey(UserInfo userInfo) {
        return waterMarkKeyRepository.findAllByUser(userInfo);
    }

    /*
     * @param: kid
     * @Description: 通过kid删除水印密钥
     */
    @Override
    public void delete(Long kid) {
        waterMarkKeyRepository.deleteByKid(kid);
    }
}
