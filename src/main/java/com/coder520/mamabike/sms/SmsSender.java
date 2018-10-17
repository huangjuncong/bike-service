package com.coder520.mamabike.sms;

/**
 * Created by 黄俊聪 on 2017/12/19.
 */
public interface SmsSender {

    void sendSms(String phone,String tplId,String params);

}
