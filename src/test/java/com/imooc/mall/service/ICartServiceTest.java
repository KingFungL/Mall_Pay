package com.imooc.mall.service;

import com.imooc.mall.MallApplicationTests;
import com.imooc.mall.form.CartAddForm;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * @author King
 * @create 2023-02-25 10:00 下午
 * @Description:
 */
public class ICartServiceTest extends MallApplicationTests {

    @Autowired
    private ICartService cartService;

    @Test
    public void add(){
        CartAddForm form = new CartAddForm();
        form.setProductId(26);
        form.setSelected(true);
        cartService.add(1, form);

    }
}