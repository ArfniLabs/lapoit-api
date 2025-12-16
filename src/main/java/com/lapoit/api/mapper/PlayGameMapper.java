package com.lapoit.api.mapper;

import com.lapoit.api.domain.PlayGameStatus;
import com.lapoit.api.dto.playgame.AdminPlayGameCreateRequest;
import com.lapoit.api.dto.playgame.AdminPlayGameResponse;
import com.lapoit.api.dto.playgame.PlayGameResponse;
import com.lapoit.api.dto.playgame.PlayGameRow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PlayGameMapper {

    void insertPlayGame(AdminPlayGameCreateRequest dto);

    Long selectLastInsertId();


    PlayGameResponse findPlayGameById(@Param("playGameId") Long playGameId);

    String findStatusById(@Param("playGameId") Long playGameId);

    void startPlayGame(Long playGameId);

    void pausePlayGame(@Param("playGameId") Long playGameId);

    void resumePlayGame(@Param("playGameId") Long playGameId);

    List<PlayGameRow> findByStoreId(Long storeId);

    /** ⭐ 스케줄러용: 진행중인 게임 조회 */
    List<PlayGameRow> findStartedGames();


    /** 레벨 업 */
    void levelUp(
            @Param("playGameId") Long playGameId,
            @Param("currentLevel") Integer currentLevel,
            @Param("nextLevel") Integer nextLevel
    );

    /** 게임 종료 */
    void finishGame(@Param("playGameId") Long playGameId);

    /** 참가자 수 증가 */
    void increaseTotalPeople(@Param("playGameId") Long playGameId);

    void decreaseNowPeople(@Param("playGameId") Long playGameId);


    void addStackOnJoin(
            @Param("playGameId") Long playGameId,
            @Param("stack") int stack
    );

}

