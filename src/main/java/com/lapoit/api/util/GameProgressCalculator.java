package com.lapoit.api.util;

import com.lapoit.api.domain.GameBlind;
import com.lapoit.api.dto.playgame.BlindDto;
import com.lapoit.api.dto.playgame.PlayGameRow;
import com.lapoit.api.dto.playgame.PlayGameStoreViewResponse;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;



@Component
public class GameProgressCalculator {

    public PlayGameStoreViewResponse toResponse(
            PlayGameRow game,
            List<GameBlind> blinds
    ) {

        // 1️⃣ 계산 불가 케이스
        if (!isCalculatable(game, blinds)) {
            return baseResponse(game);
        }

        GameBlind currentBlind = findCurrentBlind(game, blinds);
        GameBlind nextBlind = findNextBlind(game, blinds);

        long elapsedSeconds = calculateElapsedSeconds(game);
        int levelDurationSeconds = currentBlind.getDuration() * 60;

        int remainingSeconds =
                Math.max(levelDurationSeconds - (int) elapsedSeconds, 0);

        return buildResponse(
                game,
                currentBlind,
                nextBlind,
                remainingSeconds
        );
    }

    /* =========================
       계산 가능 여부
       ========================= */
    private boolean isCalculatable(PlayGameRow game, List<GameBlind> blinds) {
        return "STARTED".equals(game.getGameStatus())
                && game.getLevelStartAt() != null
                && blinds != null
                && !blinds.isEmpty();
    }

    /* =========================
       경과 시간 계산 (⭐ 핵심)
       ========================= */
    private long calculateElapsedSeconds(PlayGameRow game) {
        return Math.max(
                Duration.between(
                        game.getLevelStartAt(),
                        LocalDateTime.now()
                ).getSeconds(),
                0
        );
    }

    /* =========================
       블라인드 조회
       ========================= */
    private GameBlind findCurrentBlind(
            PlayGameRow game,
            List<GameBlind> blinds
    ) {
        return blinds.stream()
                .filter(b -> b.getLevel() == game.getGameLevel())
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException(
                                "현재 레벨 블라인드가 존재하지 않습니다. level="
                                        + game.getGameLevel()
                        )
                );
    }

    private GameBlind findNextBlind(
            PlayGameRow game,
            List<GameBlind> blinds
    ) {
        return blinds.stream()
                .filter(b -> b.getLevel() == game.getGameLevel() + 1)
                .findFirst()
                .orElse(null);
    }

    /* =========================
       응답 생성
       ========================= */
    private PlayGameStoreViewResponse buildResponse(
            PlayGameRow game,
            GameBlind currentBlind,
            GameBlind nextBlind,
            int remainingSeconds
    ) {

        return PlayGameStoreViewResponse.builder()
                .playGameId(game.getPlayGameId())
                .storeId(game.getStoreId())
                .gameSubtitle(game.getGameSubtitle())
                .gameName(game.getGameName())
                .gameStatus(game.getGameStatus())

                .startAt(game.getStartAt())
                .currentLevel(game.getGameLevel())
                .levelStartAt(game.getLevelStartAt())
                .levelDurationMinutes(currentBlind.getDuration())
                .levelRemainingSeconds(remainingSeconds)

                .currentBlind(toBlind(currentBlind))
                .nextBlind(nextBlind != null ? toBlind(nextBlind) : null)

                .totalPeople(game.getTotalPeople())
                .nowPeople(game.getNowPeople())
                .rebuyinCount(game.getRebuyinCount())
                .totalStack(game.getTotalStack())
                .averageStack(game.getAverageStack())
                .build();
    }

    private BlindDto toBlind(GameBlind blind) {
        return BlindDto.builder()
                .level(blind.getLevel())
                .smallBlind(blind.getSmallBlind())
                .bigBlind(blind.getBigBlind())
                .ante(blind.getAnte())
                .build();
    }

    /* =========================
       기본 응답 (계산 불가)
       ========================= */
    private PlayGameStoreViewResponse baseResponse(PlayGameRow game) {
        return PlayGameStoreViewResponse.builder()
                .playGameId(game.getPlayGameId())
                .storeId(game.getStoreId())
                .gameSubtitle(game.getGameSubtitle())
                .gameName(game.getGameName())
                .gameStatus(game.getGameStatus())
                .startAt(game.getStartAt())
                .totalPeople(game.getTotalPeople())
                .nowPeople(game.getNowPeople())
                .rebuyinCount(game.getRebuyinCount())
                .totalStack(game.getTotalStack())
                .averageStack(game.getAverageStack())
                .build();
    }
}

