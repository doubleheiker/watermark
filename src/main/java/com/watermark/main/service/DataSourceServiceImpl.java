package com.watermark.main.service;

import com.watermark.main.entity.DataSource;
import com.watermark.main.repository.DataSourceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class DataSourceServiceImpl implements DataSourceService{
    private final DataSourceRepository dataSourceRepository;

    public DataSourceServiceImpl(DataSourceRepository dataSourceRepository) {
        this.dataSourceRepository = dataSourceRepository;
    }

    /*
     * @param: pageNum, pageSize
     * @Description: 查找所有的数据库文件，并分页
     */
    @Override
    public Page<DataSource> getFileList(int pageNum, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, "uploadTime");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        return dataSourceRepository.findAll(pageable);
    }
}
