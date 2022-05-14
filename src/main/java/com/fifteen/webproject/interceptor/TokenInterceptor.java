package com.fifteen.webproject.interceptor;


import com.fifteen.webproject.bean.entity.User;
import com.fifteen.webproject.utils.exception.AppException;
import com.fifteen.webproject.utils.redis.RedisService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.fifteen.webproject.utils.TokenUtils.checkToken;
import static com.fifteen.webproject.utils.TokenUtils.getUserIdByJwtToken;


/**
 * @author lisihan
 * @Description TODO
 * @date 2021/11/25-21:27
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Resource
    RedisService redisService;


    /**
     * 通过拦截器对请求头进行校验
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        boolean checkToken = checkToken(request);
        System.out.println(checkToken);
        if (!checkToken) {
            throw new AppException("token不正确");
        }
        String url = request.getRequestURI();
        Integer id = getUserIdByJwtToken(request);
        User user =  (User) redisService.get(id.toString());
        if (user == null) {
            throw new AppException("token不存在");
        }
        checkRole(url,user.getRoleId());
        request.setAttribute("user", user);
        return true;
    }

    public static void checkRole(String url,Integer roleId){
        if (url.contains("/admin/")&&(roleId==1||roleId==2))throw new AppException("权限不足");
        if (url.contains("/teacher/")&&(roleId==1))throw new AppException("权限不足");
    }


}

