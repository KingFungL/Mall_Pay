package com.imooc.mall;

import com.imooc.mall.consts.MallConst;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.exception.UserLoginException;
import com.imooc.mall.pojo.User;
import com.imooc.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author King
 * @create 2023-02-13 11:57 下午
 * @Description:
 */
@Slf4j
public class UserLoginInterceptor implements HandlerInterceptor {
    /**
    * true表示继续流程，false表示中断
    * @param request
     * @param response
     * @param handler
    * @return
     * @throws Exception
    */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("preHanler...");
        User user =(User) request.getSession().getAttribute(MallConst.CURRENT_USER);
        if(user == null){
            log.info("user==null");
            throw new UserLoginException();
//            response.getWriter().print("error");
//            return false;
//            return ResponseVo.error(ResponseEnum.NEED_LOGIN);
        }
        return true;
    }
}
