package com.imooc.mall.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author King
 * @create 2023-02-24 10:22 下午
 * @Description:
 */
@Data
public class CartVo {

    private List<CartProductVo> cartProductVoList;

    private boolean selectAll;

    private BigDecimal cartTotalPrice;

    private Integer cartTotalQuantity;
}
