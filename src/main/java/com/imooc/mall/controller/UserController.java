package com.imooc.mall.controller;

import com.imooc.mall.consts.MallConst;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.form.UserForm;
import com.imooc.mall.form.UserLoginForm;
import com.imooc.mall.pojo.User;
import com.imooc.mall.service.IUserService;
import com.imooc.mall.vo.ResponseVo;
import com.sun.org.apache.xalan.internal.xsltc.dom.AbsoluteIterator;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Objects;

/**
 * @author King
 * @create 2023-01-29 11:53 下午
 * @Description:
 */
@RestController
@Slf4j
public class UserController {

//    @PostMapping("/register")
//    public void register(@RequestParam(value = "username") String userName){
//        log.info("username={}", userName);
//    }

    @Autowired
    private IUserService userService;

    @PostMapping("/user/register")
    public ResponseVo<User> register(@Valid @RequestBody UserForm userForm,
                               BindingResult bindingResult){
        User user = new User();
        BeanUtils.copyProperties(userForm, user);
        return userService.register(user);
//        log.info("username={}", userForm.getUsername());
//        //return ResponseVo.success();
//        return ResponseVo.error(ResponseEnum.NEED_LOGIN);
    }

    @PostMapping("/user/login")
    public ResponseVo<User> login(@Valid @RequestBody UserLoginForm userLoginForm,
                                  HttpSession session){

        ResponseVo<User> userResponseVo = userService.login(userLoginForm.getUsername(), userLoginForm.getPassword());

        //设置Session
        session.setAttribute(MallConst.CURRENT_USER , userResponseVo.getData());
        log.info("/login sessionId={}", session.getId());

        return userResponseVo;
    }

    //session保存在内存里，容易丢失
    @GetMapping("/user")
    public ResponseVo<User> userInfo(HttpSession session){
        log.info("/user sessionId ={}", session.getId());
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        if(user == null){
            return ResponseVo.error(ResponseEnum.NEED_LOGIN);
        }

         return ResponseVo.success(user);
    }

    /**
    * {@link org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory}
     *
    */
    @PostMapping("/user/logout")
    public ResponseVo<User> logout(HttpSession session){
        log.info("/user/logout sessionId={}", session.getId());

        session.removeAttribute(MallConst.CURRENT_USER);
        return ResponseVo.success();
    }

}
