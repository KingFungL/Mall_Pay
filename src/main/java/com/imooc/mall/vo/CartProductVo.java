package com.imooc.mall.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author King
 * @create 2023-02-24 10:24 下午
 * @Description:
 */
@Data
public class CartProductVo {

    private Integer productId;

    /**
     * 购买的数量
     */
    private Integer quantity;

    private String productName;

    private String productSubtitle;

    private String productMainImage;

    private BigDecimal productPrice;

    private Integer productStatus;

    /**
     *quantity * productPrice
     */
    private BigDecimal productTotalPrice;

    private Integer productStock;

    /**
     *商品是否选中
     */
    private Boolean productSelected;
}
