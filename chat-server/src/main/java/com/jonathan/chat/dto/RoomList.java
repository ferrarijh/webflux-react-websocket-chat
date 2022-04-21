package com.jonathan.chat.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoomList {
    private final List<RoomThumbnail> list;
}
