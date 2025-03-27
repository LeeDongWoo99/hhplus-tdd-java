package io.hhplus.tdd.point.service.fetch;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.domain.PointHistory;
import io.hhplus.tdd.point.domain.PointPolicy;
import io.hhplus.tdd.point.domain.TransactionType;
import io.hhplus.tdd.point.domain.UserPoint;
import io.hhplus.tdd.point.service.PointService;
import io.hhplus.tdd.point.service.PointServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PointFetchTest {

    @DisplayName("조회하려는 유저가 있을 경우")
    @Test
    void testGetUserPoint_success() {
        // given
        PointPolicy mockPointPolicy = mock(PointPolicy.class);
        UserPointTable mockUserPointTable = mock(UserPointTable.class);
        PointHistoryTable mockPointHistoryTable = mock(PointHistoryTable.class);
        PointService pointService = new PointServiceImpl(mockPointPolicy, mockUserPointTable, mockPointHistoryTable);

        long userId = 1L;
        UserPoint mockUserPoint = new UserPoint(userId, 1000, System.currentTimeMillis());
        when(mockUserPointTable.selectById(userId)).thenReturn(mockUserPoint);

        // when
        UserPoint result = pointService.getUserPoint(userId);

        // then
        assertNotNull(result);
        assertEquals(1000, result.point());
    }

    @DisplayName("조회하려는 유저가 없을 경우")
    @Test
    void testGetUserPoint_userNotFound() {
        // given:
        PointPolicy mockPointPolicy = mock(PointPolicy.class);
        UserPointTable mockUserPointTable = mock(UserPointTable.class);
        PointHistoryTable mockPointHistoryTable = mock(PointHistoryTable.class);
        PointService pointService = new PointServiceImpl(mockPointPolicy, mockUserPointTable, mockPointHistoryTable);

        long userId = 1L;
        when(mockUserPointTable.selectById(userId)).thenReturn(null);

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            pointService.getUserPoint(userId);
        });

        // then
        assertEquals("해당 유저를 찾을 수 없습니다.", exception.getMessage());
    }

    @DisplayName("유저가 존재하고 포인트 내역이 있는 경우")
    @Test
    void getPointHistories_success() {
        // given:
        PointPolicy mockPointPolicy = mock(PointPolicy.class);
        UserPointTable mockUserPointTable = mock(UserPointTable.class);
        PointHistoryTable mockPointHistoryTable = mock(PointHistoryTable.class);
        PointService pointService = new PointServiceImpl(mockPointPolicy, mockUserPointTable, mockPointHistoryTable);

        long userId = 1L;
        UserPoint mockUserPoint = new UserPoint(userId, 1000, System.currentTimeMillis());
        List<PointHistory> mockPointHistories = List.of(
                new PointHistory(1L, userId, 1000, TransactionType.CHARGE, System.currentTimeMillis()),
                new PointHistory(2L, userId, -500, TransactionType.USE, System.currentTimeMillis())
        );

        when(mockUserPointTable.selectById(userId)).thenReturn(mockUserPoint);
        when(mockPointHistoryTable.selectAllByUserId(userId)).thenReturn(mockPointHistories);

        // when
        List<PointHistory> result = pointService.getPointHistories(userId);

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1000, result.get(0).amount());
        assertEquals(-500, result.get(1).amount());
    }

    @DisplayName("유저가 존재하지만 포인트 내역이 없는 경우")
    @Test
    void testGetPointHistories_noHistory() {
        // given
        PointPolicy mockPointPolicy = mock(PointPolicy.class);
        UserPointTable mockUserPointTable = mock(UserPointTable.class);
        PointHistoryTable mockPointHistoryTable = mock(PointHistoryTable.class);
        PointService pointService = new PointServiceImpl(mockPointPolicy, mockUserPointTable, mockPointHistoryTable);

        long userId = 1L;
        UserPoint mockUserPoint = new UserPoint(userId, 1000, System.currentTimeMillis());

        // Mocking
        when(mockUserPointTable.selectById(userId)).thenReturn(mockUserPoint);
        when(mockPointHistoryTable.selectAllByUserId(userId)).thenReturn(Collections.emptyList()); // 빈 리스트 반환

        // when
        List<PointHistory> result = pointService.getPointHistories(userId);

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @DisplayName("유저가 존재하지 않는 경우")
    @Test
    void testGetPointHistories_userNotFound() {
        // given
        PointPolicy mockPointPolicy = mock(PointPolicy.class);
        UserPointTable mockUserPointTable = mock(UserPointTable.class);
        PointHistoryTable mockPointHistoryTable = mock(PointHistoryTable.class);
        PointService pointService = new PointServiceImpl(mockPointPolicy, mockUserPointTable, mockPointHistoryTable);

        long userId = 1L;

        // Mocking
        when(mockUserPointTable.selectById(userId)).thenReturn(null);

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            pointService.getPointHistories(userId);
        });

        // then
        assertEquals("해당 유저에 대한 포인트 내역을 찾을 수 없습니다.", exception.getMessage());
    }
}
