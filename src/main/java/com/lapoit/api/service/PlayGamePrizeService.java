package com.lapoit.api.service;

import com.lapoit.api.domain.PlayGamePrize;
import com.lapoit.api.dto.playgame.PlayGamePrizeDto;
import com.lapoit.api.dto.playgame.PlayGameRow;
import com.lapoit.api.exception.CustomException;
import com.lapoit.api.exception.ErrorCode;
import com.lapoit.api.mapper.PlayGameMapper;
import com.lapoit.api.mapper.PlayGamePrizeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class PlayGamePrizeService {

    private final PlayGameMapper playGameMapper;
    private final PlayGamePrizeMapper prizeMapper;
    private final SseService sseService;

    public List<PlayGamePrizeDto> registerPrizes(
            Long playGameId,
            List<PlayGamePrizeDto> prizes
    ) {

        // 1) 플레이 게임 존재 확인
        PlayGameRow playGame = playGameMapper.findById(playGameId);
        if (playGame == null) {
            throw new CustomException(ErrorCode.GAME_NOT_FOUND);
        }

        // 2) 기존 상금 삭제 (재입력 허용)
        prizeMapper.deleteByPlayGameId(playGameId);

        // 3) 신규 상금 등록
        for (PlayGamePrizeDto dto : prizes) {
            prizeMapper.insertPrize(
                    PlayGamePrize.builder()
                            .playGameId(playGameId)
                            .prizeRank(dto.getRank())
                            .prizeAmount(dto.getAmount())
                            .build()
            );
        }

        // 상금 입력 sse 메세지 전송
        sseService.sendToPlayGame(String.valueOf(playGameId), "PRIZE_UPDATED", Map.of("playGameId", playGameId));

        // 🔥 입력된 상금 그대로 반환
        return prizes;
    }

}

