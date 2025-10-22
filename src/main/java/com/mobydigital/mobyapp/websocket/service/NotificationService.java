package com.mobydigital.mobyapp.websocket.service;

import org.apache.kafka.common.utils.SystemScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Map;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final Logger logger = Logger.getLogger(NotificationService.class.getName());
    

    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;                

   /// constructor del simp 

    @KafkaListener(topics = "websocket", groupId = "news-web")
    public void listener(String rawJson) {
        try {

            logger.info("Entre al tryCatch");
            // El JSON que te llega lo convertís a Map
            Map<String,Object> msg = objectMapper.readValue(rawJson, new TypeReference<Map<String,Object>>(){});
            Map<String, Object> item = (Map<String, Object>) msg.get("item");
            
            if (item != null) {

            String type = (String) msg.get("type"); 

            System.out.println("IsApp: " + item.get("isMobyApp"));
            System.out.println("IsWeb: " + item.get("isMobyWeb"));

             Boolean isApp = (Boolean) item.getOrDefault("isMobyApp", false);
             Boolean isWeb = (Boolean) item.getOrDefault("isMobyWeb", false);

            // 🚀 reenviar al front tal cual (o si querés, armá otro objeto)
            if (isApp) messagingTemplate.convertAndSend("/topic/news/app", msg);
            if (isWeb) messagingTemplate.convertAndSend("/topic/news/web", msg);
           
            logger.info("WS pushed: type=" + type + " toApp=" + isApp + " toWeb=" + isWeb);
            }
            if(msg.get("type").equals("REMOVED")){
                messagingTemplate.convertAndSend("/topic/news/app", msg);
                messagingTemplate.convertAndSend("/topic/news/web", msg);
            }
        } catch (JsonProcessingException e) {
            logger.severe("JSON inválido en 'websocket': " + rawJson + " - " + e.getMessage());
            // opcional: mandar a DLQ o metricar
        }
    }


   

}
