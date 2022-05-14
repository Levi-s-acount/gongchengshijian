package com.fifteen.webproject.controller.demo;

import com.fifteen.webproject.bean.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author Fifteen
 * @Date 2022/4/9
 **/
@RestController
public class TestController {
    @GetMapping("/demo/hello")
    public String getMessage(HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        System.out.println(user.getId()+","+user.getName()+","+user.getRoleId());
        return "hello jwt";
    }
}
