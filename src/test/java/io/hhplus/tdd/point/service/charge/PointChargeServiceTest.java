package io.hhplus.tdd.point.service.charge;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.domain.PointPolicy;
import io.hhplus.tdd.point.domain.UserPoint;
import io.hhplus.tdd.point.service.PointService;
import io.hhplus.tdd.point.service.PointServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PointChargeServiceTest {

    @DisplayName("0원에서 5000원을 충전했을 때, 포인트가 5000원이 되는지 확인")
    @Test
    void chargePoint_success() {
        // given
        PointPolicy mockPointPolicy = mock(PointPolicy.class);
        UserPointTable mockUserPointTable = mock(UserPointTable.class);
        PointHistoryTable mockPointHistoryTable = mock(PointHistoryTable.class);
        PointService pointService = new PointServiceImpl(mockPointPolicy, mockUserPointTable, mockPointHistoryTable);

        // 초기 UserPoint 객체 생성 (0원, ID 1)
        UserPoint currentUserPoint = new UserPoint(1L, 0, System.currentTimeMillis());
        when(mockUserPointTable.selectById(1L)).thenReturn(currentUserPoint);

        // 충전 후 예상 포인트 (0 + 5000 = 5000)
        UserPoint updatedUserPoint = new UserPoint(1L, 5000, System.currentTimeMillis());
        when(mockUserPointTable.insertOrUpdate(1L, 5000)).thenReturn(updatedUserPoint);

        // when: 0원에서 5000원을 충전
        UserPoint result = pointService.chargePoint(1L, 5000);

        // then: 충전 후 포인트가 5000원으로 변경되었는지 확인
        assertNotNull(result);
        assertEquals(5000, result.point());

    }

    @DisplayName("충전할 유저가 없을 때, IllegalArgumentException 예외가 발생하는지 확인")
    @Test
    void chargePoint_userNotFound_throwsException() {
        // given
        PointPolicy mockPointPolicy = mock(PointPolicy.class);
        UserPointTable mockUserPointTable = mock(UserPointTable.class);
        PointHistoryTable mockPointHistoryTable = mock(PointHistoryTable.class);
        PointService pointService = new PointServiceImpl(mockPointPolicy, mockUserPointTable, mockPointHistoryTable);

        // 1L userId를 조회했을 때 null 값을 반환 하도록 설정
        when(mockUserPointTable.selectById(1L)).thenReturn(null);

        // when: chargePoint 메서드를 호출하고 예외가 발생하는지 확인
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            pointService.chargePoint(1L, 5000);
        });

        // then:
        assertEquals("해당 유저를 찾을 수 없습니다.", exception.getMessage());
    }

    @DisplayName("포인트 누적 한도 금액을 추가했을 경우")
    @Test
    void chargePoint_exceedsMaxTotalPoint() {
        // given
        PointPolicy mockPointPolicy = mock(PointPolicy.class);
        UserPointTable mockUserPointTable = mock(UserPointTable.class);
        PointHistoryTable mockPointHistoryTable = mock(PointHistoryTable.class);
        PointService pointService = new PointServiceImpl(mockPointPolicy, mockUserPointTable, mockPointHistoryTable);

        long userId = 1L;
        long chargeAmount = 200000;
        UserPoint currentUserPoint = new UserPoint(userId, 900000, System.currentTimeMillis());

        when(mockUserPointTable.selectById(userId)).thenReturn(currentUserPoint);

        doThrow(new IllegalArgumentException("최대 누적 포인트 금액을 초과할 수 없습니다. 최대 한도는 1,000,000원입니다."))
                .when(mockPointPolicy).validateChargeAndAccumulatedPoint(currentUserPoint.point(), chargeAmount);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            pointService.chargePoint(userId, chargeAmount);
        });

        // then
        assertEquals("최대 누적 포인트 금액을 초과할 수 없습니다. 최대 한도는 1,000,000원입니다.", exception.getMessage());
    }

}
