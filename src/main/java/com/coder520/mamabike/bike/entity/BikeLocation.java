package com.coder520.mamabike.bike.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * Created by 黄俊聪
 */
@Data
public class BikeLocation {

    private String id;

    private Long bikeNumber;

    private int status;

    private Double[] coordinates;

    private Double distance;

    private Bike bikeInfo;

}
