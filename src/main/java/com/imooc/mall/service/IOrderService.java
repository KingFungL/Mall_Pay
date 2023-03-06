package com.imooc.mall.service;

import com.imooc.mall.vo.OrderVo;
import com.imooc.mall.vo.ResponseVo;

/**
 * @author King
 * @create 2023-03-04 10:02 下午
 * @Description:
 */
public interface IOrderService {

    ResponseVo<OrderVo> create(Integer uid, Integer shippingId);
}
