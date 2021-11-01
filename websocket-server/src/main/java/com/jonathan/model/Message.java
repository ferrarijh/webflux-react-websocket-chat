package com.jonathan.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    private String username;
    private String content;
    private LocalDateTime date;
    private String type;
    private List<String> userList;

    public static class Type{
        public static final String USER_IN = "USER_IN";
        public static final String MESSAGE = "MESSAGE";
        public static final String USER_OUT = "USER_OUT";
        public static final String USER_LIST = "USER_LIST";
        public static final String IN_OK = "IN_OK";
        public static final String IN_REQ = "IN_REQ";
    }
}
