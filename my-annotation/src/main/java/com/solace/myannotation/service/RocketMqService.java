package com.solace.myannotation.service;

import com.solace.myannotation.enums.MqConfigEnum;

import java.util.Date;

/**
 * 作者 CG
 * 时间 2019/8/1 15:55
 * 注释 阿里云MQ接口
 */
public interface RocketMqService {
    /**
     * 作者 CG
     * 时间 2019/8/1 15:55
     * 注释 发送普通消息
     *
     * @param mqConfigEnum 发送消息类型
     * @param msg          发送消息内容
     * @param <T>
     */
    <T> void sendMsg(MqConfigEnum.Mq mqConfigEnum, T msg);

    /**
     * 作者 CG
     * 时间 2019/8/1 15:56
     * 注释 发送延迟消息
     *
     * @param mqConfigEnum 发送消息类型
     * @param msg          发送消息内容
     * @param afterTime    延迟时间秒为单位
     * @param <T>
     */
    <T> void sendDelayMsg(MqConfigEnum.Mq mqConfigEnum, T msg, Long afterTime);

    /**
     * 作者 CG
     * 时间 2019/8/1 16:13
     * 注释 定时发送
     * @param mqConfigEnum 发送消息类型
     * @param msg 发送消息内容
     * @param date 定时发送时间
     * @param <T>
     */
    <T> void sendMsgTime(MqConfigEnum.Mq mqConfigEnum, T msg, Date date);
}
