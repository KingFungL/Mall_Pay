package com.imooc.mall.service;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.MallApplicationTests;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.vo.ProductDetailVo;
import com.imooc.mall.vo.ProductVo;
import com.imooc.mall.vo.ResponseVo;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author King
 * @create 2023-02-22 10:36 下午
 * @Description:
 */
public class IProductServiceTest extends MallApplicationTests {

    @Autowired
    private IProductService productService;

    @Test
    public void list(){
        ResponseVo<PageInfo> responseVo = productService.list(null, 1, 2);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void detail(){
        ResponseVo<ProductDetailVo> responseVo = productService.detail(26);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

}