package com.codeworld.fc.common.handler;


import com.codeworld.fc.common.exception.FCException;
import com.codeworld.fc.common.response.FCResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 定义异常处理方法
 *
 * @author 16924
 */
@RestControllerAdvice
@Slf4j
public class ExceptionHand {


    /**
     * 系统异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(FCException.class)
    public FCResponse<Void> fcExceptionHandle(FCException e) {

        return FCResponse.runtimeResponse(e.getMessage());
    }

    /**
     * 参数校验失败
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public FCResponse<Void> methodArgumentNotValidExceptionHandle(MethodArgumentNotValidException exception) {
        ObjectError objectError = exception.getBindingResult().getAllErrors().get(0);
        return FCResponse.validateErrorResponse(objectError.getDefaultMessage());
    }

//    /**
//     * 认证失败异常
//     *
//     * @param exception
//     * @return
//     */
//    @ExceptionHandler(AuthenticationException.class)
//    @ResponseBody
//    public FCResponse<Void> authenticationException(AuthenticationException exception) {
//
//        return FCResponse.authErrorResponse(exception.getMessage());
//    }


}
