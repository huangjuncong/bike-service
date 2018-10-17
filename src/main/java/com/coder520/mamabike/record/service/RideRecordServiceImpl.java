package com.coder520.mamabike.record.service;

import com.coder520.mamabike.common.exception.MaMaBikeException;
import com.coder520.mamabike.record.dao.RideRecordMapper;
import com.coder520.mamabike.record.entity.RideRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by JackWangon[www.coder520.com] 2017/8/25.
 */
@Service
@Slf4j
public class RideRecordServiceImpl implements RideRecordService{

    @Autowired
    private RideRecordMapper rideRecordMapper;

    @Override
    public List<RideRecord> listRideRecord(long userId, Long lastId) throws MaMaBikeException {

        List<RideRecord> list = rideRecordMapper.selectRideRecordPage(userId,lastId);

        return list;
    }
}
