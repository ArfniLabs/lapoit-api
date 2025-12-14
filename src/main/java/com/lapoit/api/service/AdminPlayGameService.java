package com.lapoit.api.service;

import com.lapoit.api.dto.playgame.AdminPlayGameCreateRequest;
import com.lapoit.api.dto.playgame.AdminPlayGameResponse;
import com.lapoit.api.dto.playgame.PlayGameResponse;
import com.lapoit.api.exception.CustomException;
import com.lapoit.api.exception.ErrorCode;
import com.lapoit.api.mapper.GameMapper;
import com.lapoit.api.mapper.PlayGameMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminPlayGameService {

    private final GameMapper gameMapper;
    private final PlayGameMapper playGameMapper;

    public PlayGameResponse createPlayGame(AdminPlayGameCreateRequest dto) {

        if (!gameMapper.existsById(dto.getGameId())) {
           // throw new CustomException("GAME-404", "게임 템플릿이 존재하지 않습니다.");
        }

        playGameMapper.insertPlayGame(dto);
        Long playGameId = playGameMapper.selectLastInsertId();

        // ⭐ 전체 데이터 다시 조회해서 반환
        return playGameMapper.findPlayGameById(playGameId);
    }

    public PlayGameResponse startPlayGame(Long playGameId) {
        playGameMapper.startPlayGame(playGameId);

        return playGameMapper.findPlayGameById(playGameId);
    }

    public PlayGameResponse pausePlayGame(Long playGameId) {
        String status = playGameMapper.findStatusById(playGameId);

        if (!"STARTED".equals(status)) {
            //throw new CustomException("PLAY-400", "진행 중인 게임만 일시정지할 수 있습니다.");
        }

        playGameMapper.pausePlayGame(playGameId);
        return playGameMapper.findPlayGameById(playGameId);
    }

    public PlayGameResponse resumePlayGame(Long playGameId) {
        String status = playGameMapper.findStatusById(playGameId);

        if (!"PAUSED".equals(status)) {
            //throw new CustomException("PLAY-400", "일시정지 상태의 게임만 재개할 수 있습니다.");
        }

        playGameMapper.resumePlayGame(playGameId);
        return playGameMapper.findPlayGameById(playGameId);
    }
}

