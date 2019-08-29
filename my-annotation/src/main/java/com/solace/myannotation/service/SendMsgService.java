package com.solace.myannotation.service;

import com.aliyun.openservices.ons.api.Message;

public interface SendMsgService {
    void sendMq(Message message,ConfirmService confirmService);
}
