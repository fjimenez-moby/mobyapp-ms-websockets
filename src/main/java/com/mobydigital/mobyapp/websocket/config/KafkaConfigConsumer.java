package com.mobydigital.mobyapp.websocket.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.ConsumerFactory;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.IntegerDeserializer;

@Configuration
public class KafkaConfigConsumer {
// Configuraciones del consumidor de Kafka

    @Value("${BOOTSTRAP_SERVER}")
    private String bootstrapServers;

    @Value("${API_KEY}")
    private String username;

    @Value("${API_SECRET}")
    private String password;


    @Bean
    public Map<String, Object> consumerProperties() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers); // Lista de brokers de Kafka en el clúster
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "springboot-group-1"); // Identificador del grupo de consumidores
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true); // Está true :)
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100"); // Determina si se hará el commit de forma periódica a los offsets
        properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "45000"); // Tiempo de espera para detectar fallos en los consumidores
        
        // Confluent Cloud authentication
        properties.put("security.protocol", "SASL_SSL");
        properties.put("sasl.mechanism", "PLAIN");
        properties.put("sasl.jaas.config",
        String.format(
            "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"%s\" password=\"%s\";", username, password));
        
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class); // Para deserializar la llave
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class); // Para deserializar el contenido del mensaje
        return properties;
    }

    // Configuraciones del factory del consumer de Kafka
    @Bean
    public ConsumerFactory<Integer, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerProperties());
    }

    // Configuraciones del factory de listener de Kafka
    @Bean
    public ConcurrentKafkaListenerContainerFactory<Integer, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Integer, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

}
