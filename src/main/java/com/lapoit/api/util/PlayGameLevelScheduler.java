package com.lapoit.api.util;

import com.lapoit.api.domain.GameBlind;
import com.lapoit.api.dto.playgame.PlayGameRow;
import com.lapoit.api.mapper.GameBlindMapper;
import com.lapoit.api.mapper.PlayGameMapper;
import com.lapoit.api.service.AdminPlayGameService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PlayGameLevelScheduler {

    private final PlayGameMapper playGameMapper;
    private final GameBlindMapper gameBlindMapper;
    private final AdminPlayGameService adminPlayGameService;

    @Scheduled(fixedRate = 1000)
    public void autoLevelUp() {

        List<PlayGameRow> games =
                playGameMapper.findStartedGames(); // STARTED만

        for (PlayGameRow game : games) {

            GameBlind currentBlind =
                    gameBlindMapper.findByGameIdAndLevel(
                            game.getGameId(),
                            game.getGameLevel()
                    );

            if (currentBlind == null) continue;

            long elapsedSeconds =
                    Duration.between(
                            game.getLevelStartAt(),
                            LocalDateTime.now()
                    ).getSeconds()
                            - game.getLevelStopTime();

            int levelDurationSeconds =
                    currentBlind.getDuration() * 60;

            if (elapsedSeconds >= levelDurationSeconds) {
                adminPlayGameService.nextLevel(game.getPlayGameId());
            }
        }
    }

}
