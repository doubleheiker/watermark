package com.watermark.main.service;

import com.watermark.main.entity.DataSource;
import org.springframework.data.domain.Page;

public interface DataSourceService {
    Page<DataSource> getFileList(int pageNum, int pageSize);
}
