package com.jonathan.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    private String username;
    private String content;

//    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime date;
    private String type;
    private List<String> userList;

    public static class Type{
        public static final String USER_IN = "USER_IN";
        public static final String MESSAGE = "MESSAGE";
        public static final String USER_OUT = "USER_OUT";
        public static final String IN_OK = "IN_OK";
        public static final String IN_REQ = "IN_REQ";
    }
}
