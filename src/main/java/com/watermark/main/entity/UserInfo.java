package com.watermark.main.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class UserInfo {

    @Id
    @GeneratedValue
    private Long uid;

    @Column(unique = true)
    private String username;

    private String password;
    private String salt;

    /**
     * 密码盐
     * @return
     */
    public String getCredentialsSalt() {
        return this.username + this.username;
    }
}
