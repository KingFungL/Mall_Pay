package com.imooc.mall.enums;

import lombok.Getter;

/**
 * @author King
 * @create 2023-02-23 1:55 下午
 * @Description:'商品状态.1-在售 2-下架 3-删除'
 */
@Getter
public enum ProductStatusEnum {

    ON_SALE(1),

    OFF_SALE(2),

    DELETE(3),

    ;

    Integer Code;

    ProductStatusEnum(Integer code) {
        Code = code;
    }
}
