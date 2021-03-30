package com.watermark.main.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long lid;//id;

    private String username;//当时账号名称.
    private String content;//内容
    private Date date;
    private int status;//状态，操作型可为 成功1，失败0
    private int type;//操作记录0，浏览记录1，查询记录2，错误记录3
}
