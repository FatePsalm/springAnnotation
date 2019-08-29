package com.solace.myannotation.service.impl;

import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.SendResult;
import com.solace.myannotation.enums.MqConfigEnum;
import com.solace.myannotation.service.RocketMqService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;


@Component
@Service
public class RocketMqServiceImpl implements RocketMqService {
    public static final Log logger = LogFactory.getLog(RocketMqServiceImpl.class);
    @Autowired
    private Producer producer;

    @Override
    public <T> void sendMsg(MqConfigEnum.Mq mqConfigEnum, T msg) {
        Message message = assemblyMsg(mqConfigEnum, msg);
        sendMsg(message);
    }

    @Override
    public <T> void sendDelayMsg(MqConfigEnum.Mq mqConfigEnum, T msg, Long afterTime) {
        Message message = assemblyMsg(mqConfigEnum, msg);
        message.setStartDeliverTime(System.currentTimeMillis() + (afterTime * 1000));
        sendMsg(message);
    }

    @Override
    public <T> void sendMsgTime(MqConfigEnum.Mq mqConfigEnum, T msg, Date date) {
        Message message = assemblyMsg(mqConfigEnum, msg);
        message.setStartDeliverTime(date.getTime());
        sendMsg(message);
    }
     /**
       * 作者 CG
       * 时间 2019/8/2 13:54
       * 注释 封装MQ发送数据
       */
    public <T> Message assemblyMsg(MqConfigEnum.Mq mqConfigEnum, T msg) {
        Message message = null;
        try {
            message = new Message(mqConfigEnum.getTopic(), mqConfigEnum.getTags(), JSON.toJSONString(msg).getBytes("UTF-8"));
            message.setKey(mqConfigEnum.getKey());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.info("MQ消息转换bytes数组失败,消息体:" + JSON.toJSONString(msg));
        }
        return message;
    }
     /**
       * 作者 CG
       * 时间 2019/8/2 13:54
       * 注释 发送MQ消息
       */
    public void sendMsg(Message message) {
        try {
            SendResult sendResult = producer.send(message);
            if (sendResult != null) {
                logger.info(" Send mq message success. Topic is:" + message.getTopic() + " msgId is: " + message.getMsgID() + " key:" + message.getKey() + " msg:" + new String(message.getBody(), "UTF-8"));
            }
        } catch (Exception e) {
            logger.info(" Send mq message fail. Topic is:" + message.getTopic() + " msgId is: " + message.getMsgID() + " key:" + message.getKey());
            e.printStackTrace();
        }
    }
}
