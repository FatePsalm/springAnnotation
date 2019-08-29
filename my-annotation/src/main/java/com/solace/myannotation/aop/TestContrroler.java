package com.solace.myannotation.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionExecuter;
import com.aliyun.openservices.ons.api.transaction.TransactionStatus;
import com.solace.myannotation.config.rocketMq.MqConfig;
import com.solace.myannotation.service.ConfirmService;
import com.solace.myannotation.service.SendMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class TestContrroler {
    @Autowired
    private MqConfig mqConfig;
    @Autowired
    private SendMsgService sendMsgService;

    @RequestMapping("mytest")
    public JSONObject mytest(String name)  {
        JSONObject jsonObject = new JSONObject();
        System.out.println("你好北京!");
        Date date = new Date();
        jsonObject.put("data", date);
        Message message = new Message(mqConfig.getTopic(), mqConfig.getTag(), "mq send transaction message mytest".getBytes());
        sendMsgService.sendMq(message, new ConfirmService() {
            //int i = 1 / 0;
            @Override
            public void confirm(Object o) {
                System.out.println("confirm"+message);
                Message message = new Message(mqConfig.getTopic(), mqConfig.getTag(), "mq send transaction message confirm".getBytes());
                sendMsgService.sendMq(message, new ConfirmService() {
                    @Override
                    public void confirm(Object o) {
                        //int i = 1 / 0;
                        System.out.println("sendMq"+message);
                    }
                });
            }
        });
        return jsonObject;
    }


}
