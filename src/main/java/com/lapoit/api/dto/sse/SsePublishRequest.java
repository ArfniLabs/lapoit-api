package com.lapoit.api.dto.sse;

import lombok.Data;

@Data
public class SsePublishRequest {
    private String playGameId;
    private String event;
    private String data;
}
