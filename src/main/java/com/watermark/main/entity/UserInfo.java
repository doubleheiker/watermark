package com.watermark.main.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;//用户id

    @Column(unique = true)
    private String username;//账户

    private String password;//密码
    private String salt;//密码盐

    private String state;//用户状态

    //用户 - 角色 多对多
    @ManyToMany(fetch = FetchType.EAGER) //立即从数据库中进行加载数据
    @JoinTable(name = "SysUserRole", joinColumns = { @JoinColumn(name = "uid") }, inverseJoinColumns ={@JoinColumn(name = "rid") })
    private List<SysRole> roleList;

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
                ", state='" + state + '\'' +
                '}';
    }

}
