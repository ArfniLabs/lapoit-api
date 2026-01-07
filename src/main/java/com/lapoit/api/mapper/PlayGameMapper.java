package com.lapoit.api.mapper;

import com.lapoit.api.domain.PlayGameStatus;
import com.lapoit.api.dto.history.DailyJoinCountDto;
import com.lapoit.api.dto.history.GameDto;
import com.lapoit.api.dto.playgame.AdminPlayGameCreateRequest;
import com.lapoit.api.dto.playgame.AdminPlayGameResponse;
import com.lapoit.api.dto.playgame.PlayGameResponse;
import com.lapoit.api.dto.playgame.PlayGameRow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
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


    void updateLevel(@Param("playGameId") Long playGameId,
                     @Param("level") Integer level,
                     @Param("startAt") LocalDateTime startAt);


    /** 게임 종료 */
    void finishGame(@Param("playGameId") Long playGameId);



    void addStackOnJoin(
            @Param("playGameId") Long playGameId,
            @Param("stack") int stack
    );


    void addStack(
            @Param("playGameId") Long playGameId,
            @Param("stack") Integer stack
    );
    void increaseNowPeople(Long playGameId);

    void increaseRebuyinCount(Long playGameId);

    void subtractStack(
            @Param("playGameId") Long playGameId,
            @Param("stack") Integer stack
    );

    void decreaseRebuyinCount(@Param("playGameId") Long playGameId);

    void decreaseNowPeople(@Param("playGameId") Long playGameId);

    PlayGameRow findById(@Param("playGameId") Long playGameId);

    List<GameDto> selectJoinCountByStoreAndDate(
            @Param("attendanceDate") LocalDate attendanceDate
    );

    List<DailyJoinCountDto> selectDailyJoinCount(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}

