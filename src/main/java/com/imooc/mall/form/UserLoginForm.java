package com.imooc.mall.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author King
 * @create 2023-02-07 5:51 下午
 * @Description:
 */
@Data
public class UserLoginForm {

    //@NotBlank //用于String 判断空格
    //@NotEmpty 用于集合
    //@NotNull
    @NotBlank
    private String username;

    @NotBlank
    private String password;

}
