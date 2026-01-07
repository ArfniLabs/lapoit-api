package com.lapoit.api.service;

import com.lapoit.api.domain.Game;
import com.lapoit.api.domain.GameBlind;
import com.lapoit.api.domain.GameReEntry;
import com.lapoit.api.dto.game.*;
import com.lapoit.api.mapper.GameBlindMapper;
import com.lapoit.api.mapper.GameMapper;
import com.lapoit.api.mapper.GameReEntryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminGameService {

    private final GameMapper gameMapper;
    private final GameBlindMapper blindMapper;
    private final GameReEntryMapper reEntryMapper;

    public Long createGame(AdminGameCreateRequestDto dto) {

        Game game = Game.builder()
                .gameName(dto.getGameName())
                .gameStack(dto.getGameStack())
                .gamePrice(dto.getGamePrice())
                .gameLimit(dto.getGameLimit())
                .buyinEndLevel(dto.getBuyinEndLevel())
                .gameEndLevel(dto.getGameEndLevel())
                .build();

        gameMapper.insertGame(game);

        Long gameId = game.getGameId();

        dto.getBlinds().forEach(b -> {

            boolean isBreak = Boolean.TRUE.equals(b.getIsBreak());

            blindMapper.insertBlind(
                    GameBlind.builder()
                            .gameId(gameId)
                            .level(b.getLevel())

                            // 브레이크면 블라인드 값 제거
                            .smallBlind(isBreak ? null : b.getSmallBlind())
                            .bigBlind(isBreak ? null : b.getBigBlind())
                            .ante(isBreak ? null : b.getAnte())

                            // 공용 duration
                            .duration(b.getDuration())

                            .isBreak(isBreak)
                            .build()
            );
        });


        dto.getReEntries().forEach(r ->
                reEntryMapper.insertReEntry(
                        GameReEntry.builder()
                                .gameId(gameId)
                                .reEntryCount(r.getCount())
                                .reEntryPrice(r.getPrice())
                                .reEntryStack(r.getStack())
                                .build()
                )
        );

        return gameId;
    }

    public void updateGame(Long gameId, AdminGameUpdateRequestDto dto) {

        Game game = gameMapper.findById(gameId);
        if (game == null) {
            throw new RuntimeException("GAME NOT FOUND");
        }

        game.setGameName(dto.getGameName());
        game.setGameStack(dto.getGameStack());
        game.setGamePrice(dto.getGamePrice());
        game.setGameLimit(dto.getGameLimit());
        game.setBuyinEndLevel(dto.getBuyinEndLevel());
        game.setGameEndLevel(dto.getGameEndLevel());

        gameMapper.updateGame(game);

        blindMapper.deleteByGameId(gameId);
        reEntryMapper.deleteByGameId(gameId);

        dto.getBlinds().forEach(b -> {

            boolean isBreak = Boolean.TRUE.equals(b.getIsBreak());

            blindMapper.insertBlind(
                    GameBlind.builder()
                            .gameId(gameId)
                            .level(b.getLevel())

                            .smallBlind(isBreak ? null : b.getSmallBlind())
                            .bigBlind(isBreak ? null : b.getBigBlind())
                            .ante(isBreak ? null : b.getAnte())

                            .duration(b.getDuration())
                            .isBreak(isBreak)
                            .build()
            );
        });


        dto.getReEntries().forEach(r ->
                reEntryMapper.insertReEntry(
                        GameReEntry.builder()
                                .gameId(gameId)
                                .reEntryCount(r.getCount())
                                .reEntryPrice(r.getPrice())
                                .reEntryStack(r.getStack())
                                .build()
                )
        );
    }

    // 게임 템플릿 수정
    public void patchGame(Long gameId, GamePatchRequest dto) {
        gameMapper.patchGame(gameId, dto);
    }

    // 게임 블라인드 수정
    public void patchGameBlind(Long gameId, GameBlindDto dto) {

        if (Boolean.TRUE.equals(dto.getIsBreak())) {
            // 브레이크로 전환 → 블라인드 값 제거
            dto.setSmallBlind(null);
            dto.setBigBlind(null);
            dto.setAnte(null);
        }

        blindMapper.patchGameBlind(gameId, dto);
    }


    // 게임 리앤트리 수정
    public void patchGameReEntry(Long gameId, Integer count, GameReEntryPatchRequest dto) {
        reEntryMapper.patchGameReEntry(gameId, count, dto);
    }



    public void deleteGame(Long gameId) {
        blindMapper.deleteByGameId(gameId);
        reEntryMapper.deleteByGameId(gameId);
        gameMapper.deleteGame(gameId);
    }

    public List<GameResponseDto> getGameList() {

        return gameMapper.findAll().stream()
                .map(game -> {

                    // 🔹 블라인드 조회
                    List<GameBlindDto> blinds = blindMapper.findByGameId(game.getGameId())
                            .stream()
                            .map(b -> {
                                GameBlindDto dto = new GameBlindDto();
                                dto.setLevel(b.getLevel());
                                dto.setSmallBlind(b.getSmallBlind());
                                dto.setBigBlind(b.getBigBlind());
                                dto.setAnte(b.getAnte());
                                dto.setDuration(b.getDuration());
                                dto.setIsBreak(b.isBreak());
                                return dto;
                            })
                            .toList();

                    // 🔹 리엔트리 조회
                    List<GameReEntryDto> reEntries = reEntryMapper.findByGameId(game.getGameId())
                            .stream()
                            .map(r -> {
                                GameReEntryDto dto = new GameReEntryDto();
                                dto.setCount(r.getReEntryCount());
                                dto.setPrice(r.getReEntryPrice());
                                dto.setStack(r.getReEntryStack());
                                return dto;
                            })
                            .toList();

                    return GameResponseDto.builder()
                            .gameId(game.getGameId())
                            .gameName(game.getGameName())
                            .gameStack(game.getGameStack())
                            .gamePrice(game.getGamePrice())
                            .gameLimit(game.getGameLimit())
                            .buyinEndLevel(game.getBuyinEndLevel())
                            .gameEndLevel(game.getGameEndLevel())
                            .blinds(blinds)
                            .reEntries(reEntries)
                            .build();
                })
                .toList();
    }

}

