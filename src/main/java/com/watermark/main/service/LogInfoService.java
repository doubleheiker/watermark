package com.watermark.main.service;

import com.watermark.main.entity.LogInfo;
import org.springframework.data.domain.Page;

import java.util.Date;

public interface LogInfoService {
    Page<LogInfo> getLogList(int pageNum, int pageSize);
    Page<LogInfo> searchByUsername(String name, int pageNum, int pageSize);
    Page<LogInfo> searchByStatus(Integer Status, int pageNum, int pageSize);
    Page<LogInfo> searchByDate(String date, int pageNum, int pageSize);
    void delete(long lid);
    void save(String content,int type,int status);
}
