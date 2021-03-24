package com.watermark.main.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataSource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fid;
    private String hashName;//文件名
    private String originFileName;//文件原名
    private String url;//资源路径
    private String uploadTime;//上传时间

    //数据库文件与上传者是多对一的关系
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userinfo_uid")
    private UserInfo user;//上传者用户名称

}
