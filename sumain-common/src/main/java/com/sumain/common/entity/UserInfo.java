package com.sumain.common.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 登录信息接收类
 */
@Data
public class UserInfo implements Serializable {
    /**
     * 用户id
     */
    private String userId;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 用户名
     */
    private String username;
    /**
     * 手机号
     */
    private String phone;
}
