package io.hhplus.tdd.point.service;

import io.hhplus.tdd.point.domain.UserPoint;

public interface PointService {

    public UserPoint chargePoint(long userId, long chargePoint);

    public UserPoint getUserPoint(long userId);

    }



