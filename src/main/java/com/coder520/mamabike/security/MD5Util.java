package com.coder520.mamabike.security;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Created by 黄俊聪 on 2018/6/6.
 */
/**
 *@Author 黄俊聪
 *@Date 2017/12/17 13:51
 *@Description MD5加密工具类
 */
public class MD5Util {

    public static String getMD5(String source){
        return DigestUtils.md5Hex(source);
    }

}
