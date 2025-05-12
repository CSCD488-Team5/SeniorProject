package com.Team5.SeniorProject.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	// Catch all uncaught exceptions
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleGenericException(Exception ex) {
		logger.error("🔥 Unhandled exception: {}", ex.toString());
		if (ex.getStackTrace().length > 0) {
			logger.error("📍 at: {}", ex.getStackTrace()[0]); // only show top stack trace element
		}

		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", LocalDateTime.now().toString());
		body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
		body.put("error", "Internal Server Error");
		body.put("message", ex.getMessage());
		return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// Handle bad input (validation errors)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleValidationError(MethodArgumentNotValidException ex) {
		logger.warn("⚠️ Validation error: {}", ex.getBindingResult().getFieldError().getDefaultMessage());
		if (ex.getStackTrace().length > 0) {
			logger.warn("📍 at: {}", ex.getStackTrace()[0]);
		}
		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", LocalDateTime.now().toString());
		body.put("status", HttpStatus.BAD_REQUEST.value());
		body.put("error", "Validation Error");
		body.put("message", ex.getBindingResult().getFieldError().getDefaultMessage());
		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}

	// This one is super helpful for catching JSON parse errors, like when your
	// frontend sends a malformed LocalDateTime string or invalid enum value.
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Object> handleJsonParseError(HttpMessageNotReadableException ex) {
		logger.warn("📄 Malformed JSON received: {}", ex.getMostSpecificCause().getMessage());
		if (ex.getStackTrace().length > 0) {
			logger.warn("📍 at: {}", ex.getStackTrace()[0]);
		}
		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", LocalDateTime.now().toString());
		body.put("status", HttpStatus.BAD_REQUEST.value());
		body.put("error", "Malformed JSON request");
		body.put("message", ex.getMostSpecificCause().getMessage());
		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}

	// Useful when @RequestParam is required but missing.
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<Object> handleMissingParam(MissingServletRequestParameterException ex) {
		logger.warn("❗ Missing request parameter: {}", ex.getParameterName());

		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", LocalDateTime.now().toString());
		body.put("status", HttpStatus.BAD_REQUEST.value());
		body.put("error", "Missing Request Parameter");
		body.put("message", "Required parameter '" + ex.getParameterName() + "' is missing.");
		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}
}
