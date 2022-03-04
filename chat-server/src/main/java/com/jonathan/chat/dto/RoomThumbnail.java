package com.jonathan.chat.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jonathan.chat.room.LocalRoom;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
public class RoomThumbnail {
    private final String id;
    private final String title;
    private final int size;
//    private final List<String> users;
}