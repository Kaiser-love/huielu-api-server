package com.ronghui.service.common.exception;

import com.ronghui.service.common.response.ResultBean;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * 自定义异常处理器
 */
@RestControllerAdvice
@Slf4j
public class DefaultExceptionHandler {
    /**
     * 请求方式不支持
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<ResultBean> handleException(HttpRequestMethodNotSupportedException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(ResultBean.error("不支持' " + e.getMethod() + "'请求(未登录)"), HttpStatus.BAD_REQUEST);
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResultBean> notFount(RuntimeException e) {
        log.error("RuntimeException异常:", e);
        return new ResponseEntity<>(ResultBean.error(e.toString()), HttpStatus.BAD_REQUEST);
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResultBean> handleException(Exception e) {
        log.error("Exception异常", e);
        return new ResponseEntity<>(ResultBean.error(e.toString()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ResultBean> MissingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e) {
        return new ResponseEntity<>(ResultBean.error(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResultBean> HttpMessageNotReadableExceptionHandler(HttpMessageNotReadableException e) {
        return new ResponseEntity<>(ResultBean.error(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SQLGrammarException.class)
    public ResponseEntity<ResultBean> SQLGrammarExceptionHandler(SQLGrammarException e) {
        return new ResponseEntity<>(ResultBean.error(e.toString()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResultBean> DataIntegrityViolationExceptionHandler(DataIntegrityViolationException e) {
        return new ResponseEntity<>(ResultBean.error(e.toString()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ResultBean> UnauthorizedExceptionHandler(UnauthorizedException e) {
        return new ResponseEntity<>(ResultBean.error("权限不足(" + e.toString() + ")"), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ResultBean> TagServiceExceptionHandler(ApiException apiException) {
        return new ResponseEntity<>(ResultBean.error(apiException.getMessage(), apiException.getCode()), HttpStatus.BAD_REQUEST);
    }
}
