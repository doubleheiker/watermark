package com.watermark.main.repository;

import com.watermark.main.entity.LogInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Transactional
@Repository
public interface LogInfoRepository extends JpaRepository<LogInfo, Long> {
    @Override
    LogInfo save(LogInfo logInfo);
    Page<LogInfo> findByUsername(String name, Pageable pageable);
    Page<LogInfo> findByStatus(Integer status, Pageable pageable);

    @Query(value = "select * from log_info where to_char(date, 'YYYY-MM-DD') like CONCAT(:date,'%')", nativeQuery=true)
    Page<LogInfo> findByDate(@Param("date") String date, Pageable pageable);
    void deleteByLid(long id);
}
