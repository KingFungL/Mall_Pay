package com.imooc.mall.service;

import com.imooc.mall.form.CartAddForm;
import com.imooc.mall.vo.CartVo;
import com.imooc.mall.vo.ResponseVo;

/**
 * @author King
 * @create 2023-02-25 9:31 下午
 * @Description:
 */
public interface ICartService {

    ResponseVo<CartVo> add(Integer uid, CartAddForm form);

    ResponseVo<CartVo> list(Integer uid);


}
