package io.hhplus.tdd.point.domain;

import org.springframework.stereotype.Component;

@Component
public class PointPolicy {

    private static final int MAX_CHARGE_POINT = 200000;

    // 한 번에 보낼 수 있는 제한 금액을 넘었는가?
    public void validateChargeLimit(long chargeAmount) {
        if (chargeAmount > MAX_CHARGE_POINT) {
            throw new IllegalArgumentException("한 번에 충전할 수 있는 최대 금액은 200,000원입니다.");
        }
    }
}
