package com.coder520.mamabike.common.resp;

import com.coder520.mamabike.common.constants.Constants;
import lombok.Data;

/**
 * Created by 黄俊聪 on 2017/12/16.
 */
@Data
public class ApiResult<T> {

    private int code = Constants.RESP_STATUS_OK;

    private String message;

    private T data;
}
