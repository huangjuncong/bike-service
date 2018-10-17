package com.coder520.mamabike.jms;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.coder520.mamabike.sms.SmsSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Destination;

/**
 * Created by 黄俊聪 on 2017/12/19.
 */
/**
 *@Author 黄俊聪
 *@Date 2017/12/19 23:25
 *@Description ActiveMq处理器类
 */
@Component(value = "smsProcessor")
public class SmsProcessor {

    @Autowired
    private JmsMessagingTemplate jmsTemplate;

    @Autowired
    @Qualifier("verCodeService")
    private SmsSender smsSender;

    public void sendSmsToQueue(Destination destination, final String message){
        jmsTemplate.convertAndSend(destination, message);
    }

    @JmsListener(destination="sms.queue")
    public void doSendSmsMessage(String text){
        JSONObject jsonObject = JSON.parseObject(text);
        smsSender.sendSms(jsonObject.getString("mobile"),jsonObject.getString("tplId"),jsonObject.getString("vercode"));
    }


}
