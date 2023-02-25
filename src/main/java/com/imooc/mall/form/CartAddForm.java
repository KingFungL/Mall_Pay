package com.imooc.mall.form;

import javax.validation.constraints.NotNull;

/**
 * @author King
 * @create 2023-02-24 10:35 下午
 * @Description:添加商品
 */
public class CartAddForm {

    @NotNull
    private Integer productId;

    @NotNull
    private Boolean selected = true;


}
