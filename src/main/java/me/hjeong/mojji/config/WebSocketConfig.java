package me.hjeong.mojji.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
// @EnableWebSocketMessageBroker enables WebSocket message handling, backed by a message broker.
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        // enableSimpleBroker() to enable a simple memory-based message broker
        // to carry the greeting messages back to the client on destinations prefixed with /topic

        config.setApplicationDestinationPrefixes("/app");
        // It also designates the /app prefix for messages that are bound for methods annotated with @MessageMapping
        // This prefix will be used to define all the message mappings.
        // For example, /app/hello is the endpoint that the GreetingController.greeting() method is mapped to handle.
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/gs-guide-websocket").withSockJS();
    }
    // The registerStompEndpoints() method registers the /gs-guide-websocket endpoint
    // , enabling SockJS fallback options so that alternate transports can be used if WebSocket is not available.
    // The SockJS client will attempt to connect to /gs-guide-websocket and use the best available transport
    // (websocket, xhr-streaming, xhr-polling, and so on).

}
