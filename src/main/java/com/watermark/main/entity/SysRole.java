package com.watermark.main.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rid;

    private String role;// 角色标识程序中判断使用,如"admin",这个是唯一的
    private String description;//角色描述，UI界面使用
    private Boolean available = Boolean.TRUE; // 是否可用,如果不可用将不会添加给用户

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="SysRolePermission",joinColumns={@JoinColumn(name="rid")},inverseJoinColumns={@JoinColumn(name="pid")})
    private List<SysPermission> permissons;

    //用户 - 角色 多对多
    @ManyToMany
    @JoinTable(name="SysUserRole",joinColumns={@JoinColumn(name="rid")},inverseJoinColumns={@JoinColumn(name="uid")})
    private List<UserInfo> userInfos;
}
