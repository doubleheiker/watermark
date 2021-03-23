package com.watermark.main.repository;

import com.watermark.main.entity.WaterMarkKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface WaterMarkKeyRepository extends JpaRepository<WaterMarkKey, Long> {
    @Override
    WaterMarkKey save(WaterMarkKey waterMarkKey);
}
