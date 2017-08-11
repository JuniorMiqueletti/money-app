package com.juniormiqueletti.moneyapp.exceptionhandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class MoneyAppExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private MessageSource ms;

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		String userMessage = ms.getMessage("message.invalid", null, Locale.CANADA);
		String developerMessage = ex.getCause().toString() != null ? ex.getCause().toString() : ex.toString();

		List<Error> errors = Arrays.asList(new Error(userMessage, developerMessage));

		return handleExceptionInternal(ex, errors, headers, HttpStatus.BAD_REQUEST, request);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		List<Error> errors = generateErrorList(ex.getBindingResult());

		return handleExceptionInternal(ex, errors, headers, status, request);
	}

	private List<Error> generateErrorList(BindingResult bindingResult) {
		List<Error> errors = new ArrayList<>();

		bindingResult.getAllErrors().forEach(e -> {
			String userMessage = ms.getMessage(e, Locale.CANADA);
			String developerMessage = e.toString();
			errors.add(new Error(userMessage, developerMessage));
		});

		return errors;
	}
	
	@ExceptionHandler({EmptyResultDataAccessException.class})
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<Object> handleEmptyResultDataAcessException(EmptyResultDataAccessException ex, WebRequest request) {
		
		String userMessage = ms.getMessage("message.resource.notfound", null, Locale.CANADA);
		String developerMessage = ex.toString();

		List<Error> errors = Arrays.asList(new Error(userMessage, developerMessage));
		
		return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
	
	@ExceptionHandler({DataIntegrityViolationException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<Object> handleDataIntegrityConstraintViolationException(DataIntegrityViolationException ex, WebRequest request){
		String userMessage = ms.getMessage("message.resource.operationnotallowed", null, Locale.CANADA);
		String developerMessage =  ExceptionUtils.getRootCauseMessage(ex);

		List<Error> errors = Arrays.asList(new Error(userMessage, developerMessage));
		
		return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	public static class Error {

		private String userMessage;
		private String developerMessage;

		public Error(String userMessage, String developerMessage) {
			super();
			this.userMessage = userMessage;
			this.developerMessage = developerMessage;
		}

		public String getUserMessage() {
			return userMessage;
		}

		public String getDeveloperMessage() {
			return developerMessage;
		}

	}

}
