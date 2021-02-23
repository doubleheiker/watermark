package com.watermark.main.repository;

import com.watermark.main.entity.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    UserInfo findByUsername(String username);
    //Page<UserInfo> findALl(Pageable pageable);

    @Override
    UserInfo save(UserInfo userInfo);

    void deleteByUid(long uid);
    void deleteByUsername(String username);
}
