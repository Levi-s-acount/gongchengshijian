package com.fifteen.webproject.handler;


import com.fifteen.webproject.utils.exception.AppException;
import com.fifteen.webproject.utils.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author Fifteen
 * @Date 2022/1/27
 **/
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Result<Object> error(Exception e){
        e.printStackTrace();
        return new Result<>(null,false,e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = AppException.class)
    public Result<Object>error(AppException e){
        e.printStackTrace();
        return new Result<>(null,false,e.getMessage());
    }
}
