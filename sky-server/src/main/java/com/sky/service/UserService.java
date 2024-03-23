package com.sky.service;

import com.sky.entity.User;
import com.sky.vo.UserLoginVO;

import java.net.URISyntaxException;

public interface UserService {

    /**
     * 微信小程序登录
     * @param code
     * @return
     */
    User wxLogin(String code);
}
