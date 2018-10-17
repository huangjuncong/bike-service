package com.coder520.mamabike.common.exception;

import com.coder520.mamabike.common.constants.Constants;

/**
 * Created by 黄俊聪 on 2017/12/16.
 */
public class MaMaBikeException extends Exception {

    public MaMaBikeException(String message) {
        super(message);
    }

    public int getStatusCode() {
        return Constants.RESP_STATUS_INTERNAL_ERROR;
    }

}
