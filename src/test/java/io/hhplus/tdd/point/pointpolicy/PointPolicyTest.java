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
        long chargePoint = 200000;

        // when & then: 예외가 발생하지 않아야 한다.
        assertDoesNotThrow(() -> pointPolicy.validateChargeLimit(chargePoint));
    }

    @DisplayName("한 번에 충전할 수 있는 금액이 한도 금액을 초과하면 예외가 발생한다.")
    @Test
    void chargePointWithExceedingLimit() {
        // given
        long chargePoint = 250000;

        // when & then: 예외가 발생해야 한다.
        assertThrows(IllegalArgumentException.class, () -> pointPolicy.validateChargeLimit(chargePoint));
    }
}