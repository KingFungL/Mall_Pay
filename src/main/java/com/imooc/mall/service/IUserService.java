package com.imooc.mall.service;

import com.imooc.mall.pojo.User;
import com.imooc.mall.vo.ResponseVo;

/**
 * @author King
 * @create 2023-01-29 12:02 上午
 * @Description:
 */
public interface IUserService {

    /**
    * 注册
    */
    ResponseVo<User> register(User user);

    /**
     * 登陆
     */
    ResponseVo<User> login(String username, String password);
}
