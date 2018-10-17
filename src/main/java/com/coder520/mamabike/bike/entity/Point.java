package com.coder520.mamabike.bike.entity;

import lombok.Data;

/**
 * Created by 黄俊聪
 */
@Data
public class Point {

    public Point() {
    }
    public Point(Double[] loc){
        this.longitude = loc[0];
        this.latitude = loc[1];
    }
    public Point(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    private double longitude;

    private double latitude;
}
