package com.watermark.main.service;

import com.watermark.main.entity.DataSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

public interface DataSourceService {
    Page<DataSource> getFileList(int pageNum, int pageSize);
    Page<DataSource> findByOriginFileName(@Param("name") String name, int pageNum, int pageSize);
}
