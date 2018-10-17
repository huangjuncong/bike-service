package com.coder520.mamabike.bike.service;

import com.coder520.mamabike.bike.entity.BikeLocation;
import com.coder520.mamabike.common.exception.MaMaBikeException;
import com.coder520.mamabike.user.entity.UserElement;

/**
 * Created by 黄俊聪 on 2017/12/21.
 */
public interface BikeService {
    void generateBike() throws MaMaBikeException;

    void unLockBike(UserElement currentUser, Long number) throws MaMaBikeException;

    void lockBike(BikeLocation bikeLocation)throws MaMaBikeException;

    void reportLocation(BikeLocation bikeLocation)throws MaMaBikeException;
}
