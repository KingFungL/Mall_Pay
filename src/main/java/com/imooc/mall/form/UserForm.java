package com.imooc.mall.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author King
 * @create 2023-02-01 12:49 上午
 * @Description:
 */
@Data
public class UserForm {

    //@NotBlank //用于String 判断空格
    //@NotEmpty 用于集合
    //@NotNull
    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String email;
}
