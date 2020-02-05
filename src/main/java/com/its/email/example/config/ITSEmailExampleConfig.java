package com.its.email.example.config;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.PerConnectionWebSocketHandler;

import com.its.email.example.handler.SnakeWebSocketHandler;
import com.its.email.example.model.ChannelModel;
import com.its.email.example.model.EmailModel;
import com.its.email.example.model.ResponseModel;
import com.its.email.example.service.EmailServiceThread;
import com.its.email.example.service.ManagerConnectionThread;
import com.its.email.example.service.PublicEventServiceThread;

@Configuration
@EnableWebSocket
public class ITSEmailExampleConfig implements WebSocketConfigurer {
	/*
	 * dang ky 1 link websocket server voi AllowerOrigins (crossdomain - cho phep
	 * ket noi boi dns nao)
	 */
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(snakeWebSocketHandler(), "/mqtt").setAllowedOrigins("*").withSockJS()
				.setStreamBytesLimit(1000000 * 1024);
	}

	/*
	 * Dang ky class SnakeWebSocketHandler se nhan ket noi va ngat ket noi khi co
	 * tuong tac connect voi disconnect tren giao dien
	 */
	@Bean
	public WebSocketHandler snakeWebSocketHandler() {
		return new PerConnectionWebSocketHandler(SnakeWebSocketHandler.class);
	}
}
