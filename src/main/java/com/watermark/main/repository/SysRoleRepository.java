package com.watermark.main.repository;

import com.watermark.main.entity.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface SysRoleRepository extends JpaRepository<SysRole, Long> {
    public SysRole findByRid(Long id);
}
