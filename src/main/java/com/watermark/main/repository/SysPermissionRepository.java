package com.watermark.main.repository;

import com.watermark.main.entity.SysPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface SysPermissionRepository extends JpaRepository<SysPermission, Long> {
    SysPermissionRepository findOneByPid(Long id);
}
