package com.coder520.mamabike.bike.controller;

import com.coder520.mamabike.bike.entity.Bike;
import com.coder520.mamabike.bike.entity.BikeLocation;
import com.coder520.mamabike.bike.entity.Point;
import com.coder520.mamabike.bike.service.BikeGeoService;
import com.coder520.mamabike.bike.service.BikeService;
import com.coder520.mamabike.common.constants.Constants;
import com.coder520.mamabike.common.constants.Parameters;
import com.coder520.mamabike.common.exception.MaMaBikeException;
import com.coder520.mamabike.common.resp.ApiResult;
import com.coder520.mamabike.common.rest.BaseController;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * Created by 黄俊聪 on 2017/12/22.
 */
@RestController
@RequestMapping("bike")
@Slf4j
public class BikeController extends BaseController {

    @Autowired
    @Qualifier("bikeServiceImpl")
    private BikeService bikeService;

    @Autowired
    private BikeGeoService bikeGeoService;

    @Autowired
    private Parameters parameters;



    /**
     *@Author 黄俊聪
     *@Date 2017/12/22 21:29
     *@Description 生成单车
     */
    @ApiIgnore
    @RequestMapping("/generateBike")
    public ApiResult generateBike(){

        ApiResult<String> resp = new ApiResult<>();
        try {

            bikeService.generateBike();
            resp.setMessage("创建单车成功");
        } catch (MaMaBikeException e) {
            resp.setCode(e.getStatusCode());
            resp.setMessage(e.getMessage());
        } catch (Exception e) {
            log.error("Fail to update bike info", e);
            resp.setCode(Constants.RESP_STATUS_INTERNAL_ERROR);
            resp.setMessage("内部错误");
        }

        return resp;
    }

    /**
     *@Author 黄俊聪
     *@Date 2017/12/22 23:15
     *@Description 查找附近单车
     */
    @ApiOperation(value="查找附近单车",notes = "根据用户APP定位坐标来查找附近单车",httpMethod = "POST")
    @ApiImplicitParam(name = "point",value = "用户定位坐标",required = true,dataType = "Point")
    @RequestMapping("/findAroundBike")
    public ApiResult findAroundBike(@RequestBody Point point ){

        ApiResult<List<BikeLocation>> resp = new ApiResult<>();
        try {
            List<BikeLocation> bikeList = bikeGeoService.geoNear("bike-position",null,point,parameters.getLimit(),parameters.getMaxDistance());
            resp.setMessage("查询附近单车成功");
            resp.setData(bikeList);
        } catch (MaMaBikeException e) {
            resp.setCode(e.getStatusCode());
            resp.setMessage(e.getMessage());
        } catch (Exception e) {
            log.error("Fail to find around bike info", e);
            resp.setCode(Constants.RESP_STATUS_INTERNAL_ERROR);
            resp.setMessage("内部错误");
        }

        return resp;
    }

    /**
     *@Author 黄俊聪
     *@Date 2017/12/24 17:19
     *@Description
     */
    @ApiOperation(value="解锁单车",notes = "根据单车编号解锁单车",httpMethod = "POST")
    @ApiImplicitParam(name = "bike",value = "单车编号",required = true,dataType = "Bike")
    @RequestMapping("/unLockBike")
    public ApiResult unLockBike(@RequestBody Bike bike){

        ApiResult<List<BikeLocation>> resp = new ApiResult<>();
        try {
            bikeService.unLockBike(getCurrentUser(),bike.getNumber());
            resp.setMessage("等待单车解锁");
        } catch (MaMaBikeException e) {
            resp.setCode(e.getStatusCode());
            resp.setMessage(e.getMessage());
        } catch (Exception e) {
            log.error("Fail to unlock bike ", e);
            resp.setCode(Constants.RESP_STATUS_INTERNAL_ERROR);
            resp.setMessage("内部错误");
        }

        return resp;
    }

    /**
     *@Author 黄俊聪
     *@Date 2017/12/24 20:28
     *@Description 锁车骑行结束
     */
    @ApiOperation(value="锁定单车",notes = "骑行结束锁定单车（需要上传锁定时候定位坐标）",httpMethod = "POST")
    @ApiImplicitParam(name = "bikeLocation",value = "单车编号",required = true,dataType = "BikeLocation")
    @RequestMapping("/lockBike")
    public ApiResult lockBike(@RequestBody BikeLocation bikeLocation){

        ApiResult<List<BikeLocation>> resp = new ApiResult<>();
        try {
            //这里不能直接传递UserId,让自行车自己锁，这样不会造成用户一开锁，就锁上，偷骑
            bikeService.lockBike(bikeLocation);
            resp.setMessage("锁车成功");
        } catch (MaMaBikeException e) {
            resp.setCode(e.getStatusCode());
            resp.setMessage(e.getMessage());
        } catch (Exception e) {
            log.error("Fail to lock bike", e);
            resp.setCode(Constants.RESP_STATUS_INTERNAL_ERROR);
            resp.setMessage("内部错误");
        }

        return resp;
    }

    /**
     *@Author 黄俊聪
     *@Date 2017/12/26 19:34
     *@Description 单车上报坐标
     */
    @ApiOperation(value="骑行轨迹上报",notes = "骑行中上报单车位置 轨迹手机卡",httpMethod = "POST")
    @ApiImplicitParam(name = "bikeLocation",value = "单车编号",required = true,dataType = "BikeLocation")
    @RequestMapping("/reportLocation")
    public ApiResult reportLocation(@RequestBody BikeLocation bikeLocation){

        ApiResult<List<BikeLocation>> resp = new ApiResult<>();
        try {
            bikeService.reportLocation(bikeLocation);
            resp.setMessage("上报坐标成功");
        } catch (MaMaBikeException e) {
            resp.setCode(e.getStatusCode());
            resp.setMessage(e.getMessage());
        } catch (Exception e) {
            log.error("Fail to report location", e);
            resp.setCode(Constants.RESP_STATUS_INTERNAL_ERROR);
            resp.setMessage("内部错误");
        }

        return resp;
    }

}
