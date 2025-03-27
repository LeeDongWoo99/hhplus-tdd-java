package io.hhplus.tdd.point.service;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.domain.PointPolicy;
import io.hhplus.tdd.point.domain.UserPoint;
import org.springframework.stereotype.Service;

@Service
public class PointServiceImpl implements PointService{

    private final PointPolicy pointPolicy;
    private final UserPointTable userPointTable;
    private final PointHistoryTable pointHistoryTable;

    public PointServiceImpl(PointPolicy pointPolicy, UserPointTable userPointTable, PointHistoryTable pointHistoryTable) {
        this.pointPolicy = pointPolicy;
        this.userPointTable = userPointTable;
        this.pointHistoryTable = pointHistoryTable;
    }

    @Override
    public UserPoint chargePoint(long userId, long chargePoint) {
        // 충전 정책에 벗어나지 않는지 확인
        pointPolicy.validateChargeLimit(chargePoint);

        // 충전할 유저가 있는지 확인
        UserPoint currentUserPoint = userPointTable.selectById(userId);
        if (currentUserPoint == null) {
            throw new IllegalArgumentException("해당 유저를 찾을 수 없습니다.");
        }

        // 충전 후 포인트 계산
        long calculatedPoint  = currentUserPoint.point() + chargePoint;

        // 계산된 포인트 UserPoint객체에 업데이트
        UserPoint UserPoint = userPointTable.insertOrUpdate(userId, calculatedPoint);
        return UserPoint;

    }

    @Override
    public UserPoint getUserPoint(long userId) {
        // 충전할 유저가 있는지 확인
        UserPoint currentUserPoint = userPointTable.selectById(userId);
        if (currentUserPoint == null) {
            throw new IllegalArgumentException("해당 유저를 찾을 수 없습니다.");
        }

        return currentUserPoint;
    }
}
