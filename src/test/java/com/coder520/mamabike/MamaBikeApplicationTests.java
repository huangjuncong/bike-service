package com.coder520.mamabike;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.coder520.mamabike.bike.entity.BikeLocation;
import com.coder520.mamabike.bike.entity.Point;
import com.coder520.mamabike.bike.service.BikeGeoService;
import com.coder520.mamabike.bike.service.BikeService;
import com.coder520.mamabike.common.exception.MaMaBikeException;
import com.coder520.mamabike.user.entity.UserElement;
import com.coder520.mamabike.user.service.UserService;
import com.mongodb.BasicDBObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = MamaBikeApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MamaBikeApplicationTests {

	@Autowired
	@Qualifier("userService")
	private UserService userService;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	@Qualifier("bikeServiceImpl")
	private BikeService bikeService;


	@LocalServerPort
	private int port;

	@Autowired
	private BikeGeoService bikeGeoService;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Test
	public void contextLoads() {
		String result = restTemplate.getForObject("/user/hello",String.class);
		System.out.println(result);
	}

	/**
	 *@Author 黄俊聪
	 *@Date 2017/12/14 16:56
	 *@Description FastJsonHttpMessageConverter
	 */

	@Bean
	public HttpMessageConverters fastJsonHttpMessageConverters() {
		FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
		HttpMessageConverter<?> converter = fastConverter;
		return new HttpMessageConverters(converter);
	}

	@Test
	public void geoTest() throws MaMaBikeException {
		bikeGeoService.geoNearSphere("bike-position","location",
				new Point(114.062948,22.528049),0,500,null,null,10);

		bikeGeoService.geoNear("bike-position",null,new Point(114.062948, 22.528049),10,500);
//		bikeGeoService.rideContrail("ride_contrail","15034158110391291507520");
	}

	@Test
	public void unlockTest () throws MaMaBikeException {
		UserElement ue = new UserElement();
		ue.setUserId(1L);
		ue.setPushChannelId("12342");
		ue.setPlatform("android");
		bikeService.unLockBike(ue, 28000001l);
	}

//	@Test
//	public void lockTest () throws MaMaBikeException {
//
//		bikeService.lockBike(28000001l);
//	}
	@Test
	public void reportLocation() throws MaMaBikeException {
		BikeLocation location = new BikeLocation();
		location.setBikeNumber(28000001l);
		Double[] bikePosition = new Double[]{114.067296,22.534876};
		location.setCoordinates(bikePosition);
//		Query query = Query.query(Criteria.where("bike_no").is(location.getBikeNumber()));
//		Update update = Update.update("status",1)
//				.set("location.coordinates",location.getCoordinates());
//		mongoTemplate.updateFirst(query,update,"bike-position");
//		List<BasicDBObject> list = new ArrayList();
//		BasicDBObject temp = new BasicDBObject("loc",bikePosition);
//		list.add(temp);
//		BasicDBObject obj = new BasicDBObject("record_no","454754754575")
//				.append("bike_no",28000001L)
//				.append("contrail",list);
//		mongoTemplate.insert(obj,"ride_contrail");
		bikeService.reportLocation(location);
	}




}
