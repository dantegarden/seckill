package com.example.seckill.one.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("tb_user")
public class User {

    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;
    @TableField("login_name")
    private String loginName;
    @TableField("nickname")
    private String nickname;
    @TableField("password")
    private String password;
    @TableField("salt")
    private String salt;
    @TableField("mobile")
    private String mobile;
    @TableField("avatar")
    private String avatar;
    @TableField("register_date")
    private Date registerDate;
    @TableField("last_login_date")
    private Date lastLoginDate;
    @TableField("login_count")
    private Integer loginCount;
}
