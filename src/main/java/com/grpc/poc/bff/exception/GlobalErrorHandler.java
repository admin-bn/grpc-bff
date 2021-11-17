package com.grpc.poc.bff.exception;


import java.util.Arrays;

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@Configuration
//@Order(-2)
public class GlobalErrorHandler implements ErrorWebExceptionHandler {

	private ObjectMapper objectMapper;

	public GlobalErrorHandler(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	private ExceptionResponse generateExceptionObject(ServerWebExchange serverWebExchange, Throwable throwable) {

		String message = throwable.toString();
		String details = Arrays.toString(throwable.getStackTrace());
		String path = serverWebExchange.getRequest().getURI().toASCIIString() + "["
				+ serverWebExchange.getRequest().getPath().value() + "]";

		return new ExceptionResponse(message, details, path);
	}

	private DataBuffer populateDataBuffer(DataBufferFactory bufferFactory, ExceptionResponse exceptionResponse) {
		DataBuffer dataBuffer;
		try {
			dataBuffer = bufferFactory.wrap(objectMapper.writeValueAsBytes(exceptionResponse));
		} catch (JsonProcessingException e) {
			dataBuffer = bufferFactory.wrap("".getBytes());
		}
		return dataBuffer;
	}

	private Mono<Void> handleGenericException(ServerWebExchange serverWebExchange, Throwable throwable) {

		DataBufferFactory bufferFactory = serverWebExchange.getResponse().bufferFactory();
		serverWebExchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);

		ExceptionResponse ExObj = generateExceptionObject(serverWebExchange, throwable);
		DataBuffer dataBuffer = populateDataBuffer(bufferFactory, ExObj);

		serverWebExchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
		return serverWebExchange.getResponse().writeWith(Mono.just(dataBuffer));
	}

	@Override
	public Mono<Void> handle(ServerWebExchange serverWebExchange, Throwable throwable) {

		if (throwable instanceof Exception) {
			return handleGenericException(serverWebExchange, throwable);
		}

		serverWebExchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
		serverWebExchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);
		DataBuffer dataBuffer = serverWebExchange.getResponse().bufferFactory().wrap("Unknown error".getBytes());
		return serverWebExchange.getResponse().writeWith(Mono.just(dataBuffer));
	}
}