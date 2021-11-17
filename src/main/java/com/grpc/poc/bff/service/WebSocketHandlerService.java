package com.grpc.poc.bff.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;

import com.grpc.poc.bff.LogUtil;
import com.grpc.poc.bff.dto.ResponseData;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class WebSocketHandlerService implements WebSocketHandler {
	
	@Autowired
	private QrClientService service;

	@Override
	public Mono<Void> handle(WebSocketSession webSocketSession) {

		//Receive
		Flux<WebSocketMessage> receivedMessage = webSocketSession.receive();

		
		//Send
		Mono<Void> wsClientData =  webSocketSession.send(
				receivedMessage
				.map(WebSocketMessage::getPayloadAsText)
				.flatMap(this::getDataFromGrpcServer)
				.map(webSocketSession::textMessage)
				);		
		LogUtil.logInfo(this.getClass(),"websocket send the prepared data to the UI, {}",wsClientData);
		return wsClientData;
	}
	
	private Flux<String> getDataFromGrpcServer(String param) {
		
		try {
			LogUtil.logInfo(this.getClass(),"websocket received {} from UI",param);
			List<ResponseData> streamingData = service.getstreamingQrBySessionIdAsync(param);	
			ResponseData data = streamingData.get(0);	
			LogUtil.logInfo(this.getClass(),"Prepared data to be send to ui through websocket is {}",data.toString());
			return Flux.just(data.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
}