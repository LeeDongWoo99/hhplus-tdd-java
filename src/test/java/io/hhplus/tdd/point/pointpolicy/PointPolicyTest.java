package io.hhplus.tdd.point.pointpolicy;

import io.hhplus.tdd.point.domain.PointPolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PointPolicyTest {

    PointPolicy pointPolicy = new PointPolicy();

    @DisplayName("한 번에 충전할 수 있는 금액이 한도 금액을 넘지 않으면, 예외가 발생하지 않는다.")
    @Test
    void chargePointWithValidLimit() {
        // given
        long currentBalance = 100000;  // 현재 포인트 (예: 100,000원)
        long chargePoint = 200000;  // 충전하려는 금액

        // when
        assertDoesNotThrow(() -> pointPolicy.validateChargeAndAccumulatedPoint(currentBalance, chargePoint));
    }

    @DisplayName("한 번에 충전할 수 있는 금액이 한도 금액을 초과하면 예외가 발생한다.")
    @Test
    void chargePointWithExceedingLimit() {
        // given
        long currentBalance = 100000;  // 현재 포인트 (예: 100,000원)
        long chargePoint = 250000;  // 충전하려는 금액 (한도를 초과하는 금액)

        // when
        assertThrows(IllegalArgumentException.class, () -> pointPolicy.validateChargeAndAccumulatedPoint(currentBalance, chargePoint));
    }
}
