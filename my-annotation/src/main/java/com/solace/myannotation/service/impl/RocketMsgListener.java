package com.solace.myannotation.service.impl;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

@Component
public class RocketMsgListener implements MessageListener {

    public static final Log logger = LogFactory.getLog(RocketMsgListener.class);



    @Override
    public Action consume(Message message, ConsumeContext consumeContext) {
        try {
            //MQ Key
            String key = message.getKey();
            String messageId = message.getMsgID();
            //MQ boy
            byte[] body = message.getBody();
            String msg = new String(body, "UTF-8");
            logger.info("######MQ ID:" + messageId);
            logger.info("######MQ Key:" + key);
            logger.info("######MQ boy:" + msg);
            logger.info("====================================================");
            boolean results = true;

            //确认消费
            return results ? Action.CommitMessage : Action.ReconsumeLater;
        } catch (Exception e) {
            e.printStackTrace();
            return Action.ReconsumeLater;
        }
    }
}
