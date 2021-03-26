package com.watermark.main.repository;

import com.watermark.main.entity.UserInfo;
import com.watermark.main.entity.WaterMarkKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface WaterMarkKeyRepository extends JpaRepository<WaterMarkKey, Long> {
    @Override
    WaterMarkKey save(WaterMarkKey waterMarkKey);

    List<WaterMarkKey> findAllByUser(UserInfo user);
    Page<WaterMarkKey> findAllByUser(UserInfo user, Pageable pageable);

    void deleteByKid(Long kid);
}
