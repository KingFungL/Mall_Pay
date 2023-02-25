package com.imooc.mall.pojo;

import lombok.Data;

/**
 * @author King
 * @create 2023-02-25 9:52 下午
 * @Description:
 */
@Data
public class Cart {

    private Integer productId;

    private Integer quantity;

    private Boolean productSelected;

    public Cart() {
    }

    public Cart(Integer productId, Integer quantity, Boolean productSelected) {
        this.productId = productId;
        this.quantity = quantity;
        this.productSelected = productSelected;
    }
}
