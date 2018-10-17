package com.coder520.mamabike.bike.service;

import com.coder520.mamabike.bike.entity.BikeLocation;
import com.coder520.mamabike.bike.entity.Point;
import com.coder520.mamabike.common.exception.MaMaBikeException;
import com.coder520.mamabike.record.entity.RideContrail;
import com.mongodb.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 黄俊聪 on 2017/12/22.
 * 单车定位服务类 使用mogoDB
 */
@Component
@Slf4j
public class BikeGeoService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     *@Author 黄俊聪
     *@Date 2018/6/6 22:06
     *@Description 查找某经坐标点附近某范围内的坐标点，由近到远
     * @Data collection:选择哪一张表
     * locationField:映射mongodb里面的location属性
     * point:封装经纬度的的对象
     * minDistance：最短距离
     * maxDistance:最长距离
     * query：查询条件
     * fields：显示mongodb的哪些字段
     * limit：限制条数
     */
    public List<BikeLocation> geoNearSphere(String collection, String locationField, Point point,
                                            long minDistance, long maxDistance, DBObject query, DBObject fields, int limit) throws MaMaBikeException {

        try{
            if (query == null) {
                query = new BasicDBObject();
            }
            //根据db.getCollection('bike-position').find({location:{$nearSphere:{$geometry:{type:"Point",coordinates:[114.062948,22.528049]},$maxDistance:5000}},status:1})
            //来编写查询条件
            query.put(locationField,
                    new BasicDBObject("$nearSphere",
                            new BasicDBObject("$geometry",
                                    new BasicDBObject("type", "Point")
                                            .append("coordinates", new double[]{point.getLongitude(), point.getLatitude()}))
                                    .append("$minDistance", minDistance)
                                    .append("$maxDistance", maxDistance)
                    ));
            query.put("status",1);
            List<DBObject> objList = mongoTemplate.getCollection(collection).find(query, fields).limit(limit).toArray();
            List<BikeLocation> result = new ArrayList<>();
            for (DBObject obj : objList){
                BikeLocation location = new BikeLocation();
                location.setBikeNumber(((Integer)obj.get("bike_no")).longValue());
                location.setStatus((Integer) obj.get("status"));
                BasicDBList coordinates = (BasicDBList) ((BasicDBObject) obj.get("location")).get("coordinates");
                Double[] temp = new Double[2];
                coordinates.toArray(temp);
                location.setCoordinates(temp);
                result.add(location);
            }
            return result;
        }catch (Exception e) {
            log.error("fail to find around bike", e);
            throw new MaMaBikeException("查找附近单车失败");
        }

    }

    /**
     *@Author 黄俊聪
     *@Date 2018/6/6 23:02
     *@Description 查找某经坐标点附近某范围内坐标点 由近到远 并且计算距离
     */
    public List<BikeLocation> geoNear(String collection, DBObject query, Point point, int limit, long maxDistance) throws MaMaBikeException {
        try {
            if (query == null) {
                query = new BasicDBObject();
            }
            List<DBObject> pipeLine = new ArrayList<>();
/**根据下面的语句拼装查询
 * db.places.aggregate([{$geoNear: {near: { type: "Point", coordinates: [ -73.99279 , 40.719296 ] },distanceField: "dist.calculated",maxDistance: 2,query: { type: "public" },includeLocs: "dist.location",num: 5,spherical: true}}])
 */
            BasicDBObject aggregate = new BasicDBObject("$geoNear",
                    new BasicDBObject("near", new BasicDBObject("type", "Point").append("coordinates", new double[]{point.getLongitude(), point.getLatitude()}))
                            .append("distanceField", "distance")
                            .append("query", new BasicDBObject())
                            .append("num", limit)
                            .append("maxDistance", maxDistance)
                            .append("spherical", true)
                            .append("query", new BasicDBObject("status", 1))
            );
            pipeLine.add(aggregate);
            Cursor cursor = mongoTemplate.getCollection(collection).aggregate(pipeLine, AggregationOptions.builder().build());
            List<BikeLocation> result = new ArrayList<>();
            while (cursor.hasNext()) {
                DBObject obj = cursor.next();
                BikeLocation location = new BikeLocation();
                location.setBikeNumber(((Integer) obj.get("bike_no")).longValue());
                BasicDBList coordinates = (BasicDBList) ((BasicDBObject) obj.get("location")).get("coordinates");
                Double[] temp = new Double[2];
                coordinates.toArray(temp);
                location.setCoordinates(temp);
                location.setDistance((Double) obj.get("distance"));
                result.add(location);
            }

            return result;
        } catch (Exception e) {
            log.error("fail to find around bike", e);
            throw new MaMaBikeException("查找附近单车失败");
        }
    }

    public RideContrail rideContrail(String ride_contrail, String recordNo) throws MaMaBikeException {
        try {
            DBObject obj = mongoTemplate.getCollection(ride_contrail).findOne(new BasicDBObject("record_no", recordNo));
            RideContrail rideContrail = new RideContrail();
            rideContrail.setRideRecordNo((String) obj.get("record_no"));
            rideContrail.setBikeNo(((Integer) obj.get("bike_no")).longValue());
            BasicDBList locList = (BasicDBList) obj.get("contrail");
            List<Point> pointList = new ArrayList<>();
            for (Object object : locList) {
                BasicDBList locObj = (BasicDBList) ((BasicDBObject) object).get("loc");
                Double[] temp = new Double[2];
                locObj.toArray(temp);
                Point point = new Point(temp);
                pointList.add(point);
            }
            rideContrail.setContrail(pointList);
            return rideContrail;
        } catch (Exception e) {
            log.error("fail to query ride contrail", e);
            throw new MaMaBikeException("查询单车轨迹失败");
        }
    }
}
