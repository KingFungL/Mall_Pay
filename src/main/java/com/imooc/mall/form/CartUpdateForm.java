package com.imooc.mall.form;

import lombok.Data;

import javax.annotation.security.DenyAll;

/**
 * @author King
 * @create 2023-03-03 5:04 下午
 * @Description:
 */
@Data
public class CartUpdateForm {

    private Integer quantity;

    private Boolean selected;
}
