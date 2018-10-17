package com.coder520.mamabike.common.swagger;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import springfox.documentation.spring.web.json.Json;

/**
 */
public class FastJsonHttpMessageConverterEx extends FastJsonHttpMessageConverter {

    public FastJsonHttpMessageConverterEx() {
        super();
        this.getFastJsonConfig().getSerializeConfig().put(Json.class, SwaggerJsonSerializer.instance);
    }

}
