package com.example.seckill.one.exception;

import com.example.seckill.one.bean.Result;
import com.example.seckill.one.enums.ResultErrorEnum;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**全局异常处理**/
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Result<String> exceptionHandler(HttpServletRequest request, Exception e){
        if(e instanceof GlobalException) {
            GlobalException ex = (GlobalException)e;
            return Result.failByEnum(ex.getCm());
        }

        if(e instanceof BindException) {
            BindException ex = (BindException)e;
            List<ObjectError> errors = ex.getAllErrors();
            ObjectError error = errors.get(0);
            String msg = error.getDefaultMessage();
            return Result.failByEnum(ResultErrorEnum.BIND_ERROR.fillErrorMsgs(msg));
        }

        e.printStackTrace();
        return Result.failByEnum(ResultErrorEnum.SERVER_ERROR);
    }
}
