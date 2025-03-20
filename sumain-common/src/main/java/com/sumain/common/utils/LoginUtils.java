package com.sumain.common.utils;


import com.sumain.common.entity.UserInfo;

/**
 * 登录信息
 */
public class LoginUtils {
    public static ThreadLocal<UserInfo> userInfoThreadLocal = new ThreadLocal<>();
    /**
     * 获取用户信息
     */
    public static UserInfo getLoginUser() {
        return userInfoThreadLocal.get();
    }
}
