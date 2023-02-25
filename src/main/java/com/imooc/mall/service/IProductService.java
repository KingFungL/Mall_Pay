package com.imooc.mall.service;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.vo.ProductDetailVo;
import com.imooc.mall.vo.ProductVo;
import com.imooc.mall.vo.ResponseVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author King
 * @create 2023-02-22 10:22 下午
 * @Description:
 */
@Service
public interface IProductService {
    ResponseVo<PageInfo> list(Integer categoryId, Integer pageNum, Integer pageSize);

    ResponseVo<ProductDetailVo> detail(Integer productId);
}
