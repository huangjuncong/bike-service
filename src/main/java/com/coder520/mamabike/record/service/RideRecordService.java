package com.coder520.mamabike.record.service;

import com.coder520.mamabike.common.exception.MaMaBikeException;
import com.coder520.mamabike.record.entity.RideRecord;

import java.util.List;

/**
 * Created by JackWangon[www.coder520.com] 2017/8/25.
 */
public interface RideRecordService {
    List<RideRecord> listRideRecord(long userId, Long lastId) throws MaMaBikeException;
}
