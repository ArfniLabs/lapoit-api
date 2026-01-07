package com.lapoit.api.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Service
public class SseService {

    private static final long DEFAULT_TIMEOUT_MILLIS = 30 * 60 * 1000L;

    private final Map<String, Set<SseEmitter>> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(String playGameId, String userId) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT_MILLIS);
        emitters.computeIfAbsent(playGameId, key -> new CopyOnWriteArraySet<>()).add(emitter);

        emitter.onCompletion(() -> removeEmitter(playGameId, emitter));
        emitter.onTimeout(() -> removeEmitter(playGameId, emitter));
        emitter.onError(ex -> removeEmitter(playGameId, emitter));

        sendInternal(emitter, "connected", Map.of(
                "playGameId", playGameId,
                "userId", userId,
                "connectedAt", Instant.now().toString()
        ));

        return emitter;
    }

    public int sendToPlayGame(String playGameId, String eventName, Object data) {
        Set<SseEmitter> playGameEmitters = emitters.get(playGameId);
        if (playGameEmitters == null || playGameEmitters.isEmpty()) {
            return 0;
        }

        int sent = 0;
        for (SseEmitter emitter : playGameEmitters) {
            if (sendInternal(emitter, eventName, data)) {
                sent++;
            }
        }

        if (sent == 0) {
            emitters.remove(playGameId);
        }

        return sent;
    }

    private boolean sendInternal(SseEmitter emitter, String eventName, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .name(eventName)
                    .data(data));
            return true;
        } catch (IOException | IllegalStateException ex) {
            emitter.complete();
            return false;
        }
    }

    private void removeEmitter(String playGameId, SseEmitter emitter) {
        Set<SseEmitter> playGameEmitters = emitters.get(playGameId);
        if (playGameEmitters == null) {
            return;
        }
        playGameEmitters.remove(emitter);
        if (playGameEmitters.isEmpty()) {
            emitters.remove(playGameId);
        }
    }
}
