package com.coder520.mamabike.record.entity;

import com.coder520.mamabike.bike.entity.Point;
import lombok.Data;

import java.util.List;

/**
 * 骑行轨迹
 */
@Data
public class RideContrail {

    private String rideRecordNo;

    private Long bikeNo;

    private List<Point> contrail;

}
