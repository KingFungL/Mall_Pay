package com.imooc.mall.enums;

import lombok.Getter;

/**
 * @author King
 * @create 2023-01-29 11:36 下午
 * @Description:'角色0-管理员,1-普通用户'
 */
@Getter
public enum RoleEnum {
    ADMIN(0),

    CUSTOMER(1),

    ;

    Integer code;

    RoleEnum(Integer code){
        this.code = code;
    }
}
