package com.watermark.main.repository;

import com.watermark.main.entity.DataSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface DataSourceRepository extends JpaRepository<DataSource, Long> {
    @Override
    DataSource save(DataSource dataSource);
    
    Page<DataSource> findAll(Pageable pageable);
    DataSource findByFid(Long id);

    @Query(value = "select * from data_source where origin_file_name like CONCAT('%',:name,'%')", nativeQuery=true)
    Page<DataSource> findByOriginFileName(@Param("name") String name, Pageable pageable);
}
