package com.jonathan.chat.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonathan.chat.dto.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppMapper {

    private final ObjectMapper mapper;

    public ChatMessage readChatMessage(String inbound){
        try{
            return mapper.readValue(inbound, ChatMessage.class);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return ChatMessage.builder().build();
    }

    public String write(Object outbound){
        try{
            return mapper.writeValueAsString(outbound);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public <T> T read(String str, TypeReference<T> type) {
        try {
            return mapper.readValue(str, type);
        }catch(JsonProcessingException jpe){
            jpe.printStackTrace();
        }
        return null;
    }
}
