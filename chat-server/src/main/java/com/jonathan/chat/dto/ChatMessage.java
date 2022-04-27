package com.jonathan.chat.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
//@NoArgsConstructor
public class ChatMessage {
    private String username;
    private String content;
    private LocalDateTime date;
    private String type;
    private List<String> userList;

    public enum Type{
        INIT("INIT"),
        USER_IN("USER_IN"),
        MESSAGE("MESSAGE"),
        USER_OUT("USER_OUT"),

        IN_OK("IN_OK"),
        IN_REQ("IN_REQ");

        private final String value;
        Type(String v){
            this.value = v;
        }

        public String getValue() {
            return value;
        }
    }
}
