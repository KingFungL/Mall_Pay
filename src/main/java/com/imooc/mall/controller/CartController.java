package com.imooc.mall.controller;

import com.imooc.mall.form.CartAddForm;
import com.imooc.mall.vo.CartVo;
import com.imooc.mall.vo.ResponseVo;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @author King
 * @create 2023-02-24 10:36 下午
 * @Description:
 */
@RestController
public class CartController {

    @PostMapping("/carts")
    public ResponseVo<CartVo> add(@Valid @RequestBody CartAddForm cartAddForm){
        return null;
    }
}
