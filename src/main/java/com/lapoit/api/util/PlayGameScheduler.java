package com.lapoit.api.util;

import com.lapoit.api.domain.GameBlind;
import com.lapoit.api.dto.playgame.PlayGameRow;
import com.lapoit.api.mapper.GameBlindMapper;
import com.lapoit.api.mapper.PlayGameMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PlayGameScheduler {

    private final PlayGameMapper playGameMapper;
    private final GameBlindMapper gameBlindMapper;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void advanceLevels() {

        List<PlayGameRow> games =
                playGameMapper.findStartedGames();

        LocalDateTime now = LocalDateTime.now();

        for (PlayGameRow game : games) {

            GameBlind blind =
                    gameBlindMapper.findByGameIdAndLevel(
                            game.getGameId(),
                            game.getGameLevel()
                    );

            if (blind == null) continue;

            long elapsed =
                    Duration.between(game.getLevelStartAt(), now).getSeconds()
                            - game.getTotalStopTime();

            if (elapsed >= blind.getDuration() * 60) {

                GameBlind next =
                        gameBlindMapper.findByGameIdAndLevel(
                                game.getGameId(),
                                game.getGameLevel() + 1
                        );

                if (next == null) {
                    playGameMapper.finishGame(game.getPlayGameId());
                } else {
                    playGameMapper.levelUp(
                            game.getPlayGameId(),
                            next.getLevel()
                    );
                }
            }
        }
    }
}
