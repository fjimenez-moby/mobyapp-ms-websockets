package com.mobydigital.mobyapp.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
//Clase donde continene la configuración para WebSockets
@Configuration
@EnableWebSocketMessageBroker //Activa el soporte para el manejo de mensajes de WebSockets
public class WebSocketsConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) { //Método que configura el broker de mensajes, encargado de enrutar los mensajes entre los clientes y el servidor.
        config.enableSimpleBroker("/topic"); //Habilita un broker de mensajes en memoria. Los clientes se suscribien a los destinos con el prefijo /topic para recibir mensajes.
        config.setApplicationDestinationPrefixes("/app"); //Prefijo para los mensajes que se envian desde el cliente al servidor.
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) { //Registra los endpoints que los clientes usarán para establecer la conexión WebSocket.
        registry.addEndpoint("/ws") //Se inicia la conexión WebSocket con el servidor
                .setAllowedOrigins("http://localhost:9000","http://localhost:4200")
                .withSockJS(); // Habilita el soporte SockJS
    }
}