package io.hhplus.tdd.point.service.use;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.domain.PointPolicy;
import io.hhplus.tdd.point.domain.UserPoint;
import io.hhplus.tdd.point.service.PointService;
import io.hhplus.tdd.point.service.PointServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PointUseTest {

    @DisplayName("포인트 사용 시, 정상적인 동작 확인")
    @Test
    void usePoint_success() {
        // given
        PointPolicy mockPointPolicy = mock(PointPolicy.class);
        UserPointTable mockUserPointTable = mock(UserPointTable.class);
        PointHistoryTable mockPointHistoryTable = mock(PointHistoryTable.class);
        PointService pointService = new PointServiceImpl(mockPointPolicy, mockUserPointTable, mockPointHistoryTable);

        long userId = 1L;
        long initialPoint = 5000;
        long usePoint = 3000;

        UserPoint mockUserPoint = new UserPoint(userId, initialPoint, System.currentTimeMillis());
        when(mockUserPointTable.selectById(userId)).thenReturn(mockUserPoint);

        UserPoint updatedUserPoint = new UserPoint(userId, initialPoint - usePoint, System.currentTimeMillis());
        when(mockUserPointTable.insertOrUpdate(userId, initialPoint - usePoint)).thenReturn(updatedUserPoint);

        // when
        UserPoint result = pointService.usePoint(userId, usePoint);

        // then
        assertNotNull(result);
        assertEquals(initialPoint - usePoint, result.point());
    }

    @DisplayName("포인트 사용 시, 유저가 존재하지 않으면 예외 발생")
    @Test
    void usePoint_userNotFound_throwsException() {
        // given
        PointPolicy mockPointPolicy = mock(PointPolicy.class);
        UserPointTable mockUserPointTable = mock(UserPointTable.class);
        PointHistoryTable mockPointHistoryTable = mock(PointHistoryTable.class);
        PointService pointService = new PointServiceImpl(mockPointPolicy, mockUserPointTable, mockPointHistoryTable);

        long userId = 1L;
        long usePoint = 3000;

        when(mockUserPointTable.selectById(userId)).thenReturn(null);

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            pointService.usePoint(userId, usePoint);
        });

        // then
        assertEquals("해당 유저를 찾을 수 없습니다.", exception.getMessage());
    }

}
