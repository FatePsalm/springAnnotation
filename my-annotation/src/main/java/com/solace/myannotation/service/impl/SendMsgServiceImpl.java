package com.solace.myannotation.service.impl;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.exception.ONSClientException;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionExecuter;
import com.aliyun.openservices.ons.api.transaction.TransactionProducer;
import com.aliyun.openservices.ons.api.transaction.TransactionStatus;
import com.solace.myannotation.config.rocketMq.MqConfig;
import com.solace.myannotation.service.ConfirmService;
import com.solace.myannotation.service.SendMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SendMsgServiceImpl implements SendMsgService {
    @Autowired
    private MqConfig mqConfig;
    @Autowired
    private TransactionProducer transactionProducer;

    @Override
    public void sendMq(Message message, ConfirmService confirmService) {
        try {
            SendResult sendResult = transactionProducer.send(message, new LocalTransactionExecuter() {
                @Override
                public TransactionStatus execute(Message message, Object o) {
                    try {
                        ConfirmService service = (ConfirmService) o;
                        service.confirm(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return TransactionStatus.RollbackTransaction;
                    }
                    return TransactionStatus.CommitTransaction;
                }
            }, confirmService);
            assert sendResult != null;
        } catch (ONSClientException e) {
            // 消息发送失败，需要进行重试处理，可重新发送这条消息或持久化这条数据进行补偿处理
            System.out.println(new Date() + " Send mq message failed! Topic is:" + mqConfig.getTopic());
            e.printStackTrace();
        }
    }
}
