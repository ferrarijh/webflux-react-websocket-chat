package com.jonathan.chat.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonathan.chat.dto.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppMapper {

    private final ObjectMapper mapper;

    public ChatMessage read(String inbound){
        ChatMessage res = ChatMessage.builder().build();
        try{
            res = mapper.readValue(inbound, ChatMessage.class);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return res;
    }

    public String write(ChatMessage outbound){
        String res = "";
        try{
            res = mapper.writeValueAsString(outbound);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return res;
    }
}
