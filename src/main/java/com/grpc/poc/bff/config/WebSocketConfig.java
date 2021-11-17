package com.grpc.poc.bff.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.server.WebSocketService;
import org.springframework.web.reactive.socket.server.support.HandshakeWebSocketService;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import org.springframework.web.reactive.socket.server.upgrade.ReactorNettyRequestUpgradeStrategy;

import com.grpc.poc.bff.LogUtil;
import com.grpc.poc.bff.service.WebSocketHandlerService;

@Configuration
public class WebSocketConfig {

	@Autowired
	//@Qualifier("ReactiveWebSocketHandler")
    private WebSocketHandlerService webSocketHandlerService;

    @Bean
    public HandlerMapping handlerMapping(){
        Map<String, WebSocketHandlerService> handlerMap = Map.of("/qrcode", webSocketHandlerService);
        LogUtil.logInfo(this.getClass(),"websocket  handler is running at the endpoint - {}","/qrcode");
        return new SimpleUrlHandlerMapping(handlerMap, 1);
    }
    
  

    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter(webSocketService());//
    }
    
    @Bean
    public WebSocketService webSocketService() {
        return new HandshakeWebSocketService(new ReactorNettyRequestUpgradeStrategy());
    }
  
}
