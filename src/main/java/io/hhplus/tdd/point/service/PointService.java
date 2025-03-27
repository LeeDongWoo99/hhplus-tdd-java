package io.hhplus.tdd.point.service;

import io.hhplus.tdd.point.domain.PointHistory;
import io.hhplus.tdd.point.domain.UserPoint;

import java.util.List;

public interface PointService {

    public UserPoint chargePoint(long userId, long chargePoint);

    public UserPoint getUserPoint(long userId);

    public List<PointHistory> getPointHistories(long userId);

    }



