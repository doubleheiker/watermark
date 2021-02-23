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
    private Long uid;//用户id

    @Column(unique = true)
    private String username;//账户

    private String password;//密码
    private String salt;//密码盐

    private String perms;//权限

    /**
     * 密码盐
     * @return
     */
    public String getCredentialsSalt() {
        return this.username + this.salt;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "uid=" + uid +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", perms='" + perms + '\'' +
                '}';
    }

}
