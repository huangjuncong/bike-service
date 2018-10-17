package com.coder520.mamabike.record.dao;

import com.coder520.mamabike.record.entity.RideRecord;

import java.util.List;

public interface RideRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RideRecord record);

    int insertSelective(RideRecord record);

    RideRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RideRecord record);

    int updateByPrimaryKey(RideRecord record);

    RideRecord selectRecordNotClosed(long userId);

    RideRecord selectBikeRecordOnGoing(Long bikeNo);

    List<RideRecord> selectRideRecordPage(long userId, Long lastId);
}