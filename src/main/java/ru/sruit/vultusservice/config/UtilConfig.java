package ru.sruit.vultusservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilConfig {

    @Bean
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public JacksonJsonParser getJacksonParser() {
        return new JacksonJsonParser();
    }
}
