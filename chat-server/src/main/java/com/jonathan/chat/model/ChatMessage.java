package com.jonathan.chat.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
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
