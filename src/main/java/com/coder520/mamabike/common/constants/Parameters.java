package com.coder520.mamabike.common.constants;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by 黄俊聪 on 2017/12/16.
 */
@Component
@Data
public class Parameters {

    /*****redis config start*******/
    @Value("${redis.host}")
    private String redisHost;
    @Value("${redis.port}")
    private int redisPort;
    @Value("${redis.auth}")
    private String redisAuth;
    @Value("${redis.max-idle}")
    private int redisMaxTotal;
    @Value("${redis.max-total}")
    private int redisMaxIdle;
    @Value("${redis.max-wait-millis}")
    private int redisMaxWaitMillis;
    /*****redis config end*******/


    @Value("#{'${security.noneSecurityPath}'.split(',')}")
    private List<String> noneSecurityPath;

    /********#查找附近单车的参数********/
    @Value("${limit}")
    private int limit;

    @Value("${maxDistance}")
    private Integer maxDistance;

    /********#查找附近单车的参数********/

}
