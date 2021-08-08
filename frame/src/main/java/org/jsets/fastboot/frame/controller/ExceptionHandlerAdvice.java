package org.jsets.fastboot.frame.controller;

import org.jsets.fastboot.frame.model.Respond;
import org.jsets.fastboot.security.exception.ForbiddenException;
import org.jsets.fastboot.security.exception.RuntimeForbiddenException;
import org.jsets.fastboot.security.exception.RuntimeUnauthorizedException;
import org.jsets.fastboot.security.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import lombok.extern.slf4j.Slf4j;

/**
 * 异常处理
 *
 * @author wangjie (https://github.com/wj596)
 * @date 2021.07.10 21:57
 * @since 0.1
 */
@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(IllegalArgumentException.class)
	public Respond handleIllegalArgumentException(IllegalArgumentException e) {
		log.error(e.getMessage(), e);
		return Respond.fail(HttpStatus.BAD_REQUEST, e.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Respond handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		log.error(e.getMessage(), e);
		return Respond.fail(HttpStatus.BAD_REQUEST, e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
	}

	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(RuntimeUnauthorizedException.class)
	public Respond runtimeUnauthorizedException(RuntimeUnauthorizedException e) {
		log.error(e.getMessage(), e);
		return Respond.fail(HttpStatus.UNAUTHORIZED, e.getMessage());
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(RuntimeForbiddenException.class)
	public Respond runtimeForbiddenException(RuntimeForbiddenException e) {
		log.error(e.getMessage(), e);
		return Respond.fail(HttpStatus.FORBIDDEN, e.getMessage());
	}

	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(UnauthorizedException.class)
	public Respond authenticationException(UnauthorizedException e) {
		log.error(e.getMessage(), e);
		return Respond.fail(HttpStatus.UNAUTHORIZED, e.getMessage());
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(ForbiddenException.class)
	public Respond forbiddenException(ForbiddenException e) {
		log.error(e.getMessage(), e);
		return Respond.fail(HttpStatus.FORBIDDEN, e.getMessage());
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value = Exception.class)
	public Respond exception(Exception exception) {
		log.error(exception.getMessage(), exception);
		return Respond.fail(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
	}
}