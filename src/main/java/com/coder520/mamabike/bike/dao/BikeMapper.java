package com.coder520.mamabike.bike.dao;

import com.coder520.mamabike.bike.entity.Bike;
import com.coder520.mamabike.bike.entity.BikeNoGen;

public interface BikeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Bike record);

    int insertSelective(Bike record);

    Bike selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Bike record);

    int updateByPrimaryKey(Bike record);

    void generateBikeNo(BikeNoGen bikeNoGen);

    Bike selectByBikeNo(Long bikeNo);
}