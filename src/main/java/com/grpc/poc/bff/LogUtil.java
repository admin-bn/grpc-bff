package com.grpc.poc.bff;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtil {
	
	private LogUtil() {}
	
	public static void logInfo(Class<?> clasz,String message, Object... arguments) {
		Logger logger = LoggerFactory.getLogger(clasz);
		message += " {{" + getCurrentDateTime() +"}}";
		logger.info(message, arguments);
	}
	
	public static String getCurrentDateTime() {
		final LocalDateTime now = LocalDateTime.now();
		return now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss", Locale.ENGLISH));
	}

}
