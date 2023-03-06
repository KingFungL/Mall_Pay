package com.imooc.mall.enums;

import lombok.Getter;

/**
 * @author King
 * @create 2023-03-06 7:58 下午
 * @Description:
 */
@Getter
public enum PaymentTypeEnum {

    PAY_ONLINE(1),
    ;

    Integer code;

    PaymentTypeEnum(Integer code) {
        this.code = code;
    }
}
