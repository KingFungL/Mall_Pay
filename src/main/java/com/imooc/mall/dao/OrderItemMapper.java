package com.imooc.mall.dao;

import com.imooc.mall.pojo.OrderItem;
import org.apache.ibatis.annotations.Mapper;


public interface OrderItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);
}