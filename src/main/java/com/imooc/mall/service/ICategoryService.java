package com.imooc.mall.service;

import com.imooc.mall.vo.CategoryVo;
import com.imooc.mall.vo.ResponseVo;

import java.util.List;

/**
 * @author King
 * @create 2023-02-14 10:07 下午
 * @Description:
 */
public interface ICategoryService {

    ResponseVo<List<CategoryVo>> selectAll();
}
