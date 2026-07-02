package com.barber.exception;

import cn.dev33.satoken.exception.SaTokenException;
import cn.dev33.satoken.util.SaResult;
import com.barber.tool.data.ResultVO;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * ClassName: GlobalExceptionHandler
 * Description: 异常处理
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * SA 令牌异常处理程序
     * SA Token Exception Handler
     * @param e E
     * @return {@link SaResult}
     */
    @ExceptionHandler(SaTokenException.class)
    public SaResult saTokenExceptionHandler(SaTokenException e) {
        return SaResult.code(e.getCode());
    }


    /**
     * 异常处理程序
     * Exception Handler
     * @param e e
     * @return {@link ResultVO}
     */
    @ExceptionHandler(Exception.class)
    public ResultVO exceptionHandler(Exception e) {

        ResultVO resultVO = new ResultVO();
        resultVO.setSuccess(false);
        resultVO.setErrorDesc(e.getMessage());

        return resultVO;
    }

}
