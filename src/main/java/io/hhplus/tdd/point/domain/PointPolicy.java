package io.hhplus.tdd.point.domain;

import org.springframework.stereotype.Component;

@Component
public class PointPolicy {
    private static final int MAX_CHARGE_POINT = 200_000;  // 한 번에 충전할 수 있는 최대 금액
    private static final int MAX_TOTAL_POINT = 1_000_000; // 포인트 누적 금액 한도

    public void validateChargeAndAccumulatedPoint(long currentBalance, long chargeAmount) {
        // 한 번에 충전할 수 있는 금액 검증
        if (chargeAmount > MAX_CHARGE_POINT) {
            throw new IllegalArgumentException("한 번에 충전할 수 있는 최대 금액은 200,000원입니다.");
        }

        // 포인트 누적 금액 한도 검증
        long newBalance = currentBalance + chargeAmount;
        if (newBalance > MAX_TOTAL_POINT) {
            throw new IllegalArgumentException("최대 누적 포인트 금액을 초과할 수 없습니다. 최대 한도는 1,000,000원입니다.");
        }
    }
}
