package com.watermark.main.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WaterMarkKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kid;
    private String key;
    private Double M;
    private Integer markedLine;

    //水印密钥 - 文件fid 一对一
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "datasource_fid")
    private DataSource file;

    //水印密钥 - 用户 多对一
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userinfo_uid")
    private UserInfo user;//上传者用户名称
}
