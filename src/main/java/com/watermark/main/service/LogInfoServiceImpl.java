package com.watermark.main.service;

import com.watermark.main.entity.LogInfo;
import com.watermark.main.entity.UserInfo;
import com.watermark.main.repository.LogInfoRepository;
import org.apache.shiro.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class LogInfoServiceImpl implements LogInfoService{
    private final LogInfoRepository logInfoRepository;

    public LogInfoServiceImpl(LogInfoRepository logInfoRepository) {
        this.logInfoRepository = logInfoRepository;
    }

    @Override
    public Page<LogInfo> getLogList(int pageNum, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, "date");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        return logInfoRepository.findAll(pageable);
    }

    @Override
    public Page<LogInfo> searchByUsername(String name, int pageNum, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, "date");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        return logInfoRepository.findByUsername(name, pageable);
    }

    @Override
    public Page<LogInfo> searchByStatus(Integer Status, int pageNum, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, "date");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        return logInfoRepository.findByStatus(Status, pageable);
    }

    @Override
    public Page<LogInfo> searchByDate(String date, int pageNum, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, "date");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        return logInfoRepository.findByDate(date, pageable);
    }

    @Override
    public void delete(long lid) {
        logInfoRepository.deleteByLid(lid);
    }

    @Override
    public void save(String content, int status, int type) {
        //状态，操作型可为 成功1，失败0
        //操作记录0，浏览记录1，查询记录2，错误记录3
        //获取上传者用户名
        UserInfo user = (UserInfo) SecurityUtils.getSubject().getPrincipal();
        String username = "游客";
        if (user != null) {
            username = user.getUsername();
        }
        LogInfo logInfo=new LogInfo();
        logInfo.setContent(content);
        logInfo.setType(type);
        logInfo.setStatus(status);

        Date dNow= new Date();

        logInfo.setDate(dNow);
        logInfo.setUsername(username);
        logInfoRepository.save(logInfo);
    }
}
