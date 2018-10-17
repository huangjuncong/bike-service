package com.coder520.mamabike.common.constants;

import lombok.Data;

/**
 * Created by 黄俊聪 on 2017/12/16.
 */
@Data
public class Constants {

    /**自定义状态码 start**/
    public static final int RESP_STATUS_OK = 200;

    public static final int RESP_STATUS_NOAUTH = 401;

    public static final int RESP_STATUS_INTERNAL_ERROR = 500;

    public static final int RESP_STATUS_BADREQUEST = 400;

    public static final String REQUEST_TOKEN_KEY = "user-token";
    public static final String REQUEST_VERSION_KEY = "version";
    /**自定义状态码 end**/

    /**秒滴SMS start**/
    public static final String MDSMS_ACCOUNT_SID = "1af57b07a3484ee4bdbb126a669569b0";

    public static final String MDSMS_AUTH_TOKEN = "a8620e9bf28547d1a0f3dac7a07e6ecb";

    public static final String MDSMS_REST_URL = "https://api.miaodiyun.com/20150822";

    public static final String MDSMS_VERCODE_TPLID = "120798080";

    /**秒滴SMS end**/

    /***七牛keys start****/
    public static final String QINIU_ACCESS_KEY="nXKUE7HxHXLSCpOzgqaPa7Sd-EIHFU_aQHQhhQSN";

    public static final String QINIU_SECRET_KEY="4u3WHoSSZu7R9klq5P5My3dRBrO2y4VJ03nzGsAa";

    public static final String QINIU_HEAD_IMG_BUCKET_NAME="mamabike";

    public static final String QINIU_HEAD_IMG_BUCKET_URL="p1b1t3sak.bkt.clouddn.com";

    /***七牛keys end****/

    /**百度云推送 start**/
    public static final String BAIDU_YUN_PUSH_API_KEY="Mm6jQZ4ndr36TGa2n2LoXVqB";

    public static final String BAIDU_YUN_PUSH_SECRET_KEY="kjxkaO9RZpeqGyBLrUo0ngTSfPAKW3qo";

    public static final String CHANNEL_REST_URL = "api.push.baidu.com";
    /**百度云推送end**/
}
