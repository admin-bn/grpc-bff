package com.grpc.poc.bff.exception;

import java.util.Date;

public class ExceptionResponse{
	
	private Date timestamp;
	private String message;
	private String details;
	private String uri;
	
	public ExceptionResponse() {
		super();
		this.timestamp = new Date();
	}
	
	public ExceptionResponse(String message) {
		this();
		this.message = message;
	}
	
	public ExceptionResponse(String message, String details) {
		this(message);
		this.details = details;
	}
	public ExceptionResponse(String message, String details, String uri) {
		this(message,details);
		this.uri = uri;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	
	public String getMessage() {
		return message;
	}

	
	public String getDetails() {
		return details;
	}

	public String getUri() {
		return uri;
	}
}
