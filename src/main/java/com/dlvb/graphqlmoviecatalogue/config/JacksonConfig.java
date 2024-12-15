package com.dlvb.graphqlmoviecatalogue.config;

import com.dlvb.graphqlmoviecatalogue.jackson.ProtobufMessageSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.protobuf.Message;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JacksonConfig {

    @NonNull
    private final ProtobufMessageSerializer protobufMessageSerializer;

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new SimpleModule().addSerializer(Message.class, protobufMessageSerializer));
        return objectMapper;
    }

}
