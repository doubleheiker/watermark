package com.watermark.main.repository;

import com.watermark.main.entity.DataSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface DataSourceRepository extends JpaRepository<DataSource, Long> {
    @Override
    DataSource save(DataSource dataSource);
    
    Page<DataSource> findAll(Pageable pageable);
    DataSource findByFid(Long id);
}
