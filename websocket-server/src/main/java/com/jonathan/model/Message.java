package com.jonathan.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class Message {
    private String id;
    private String content;
    private LocalDateTime date;
}
