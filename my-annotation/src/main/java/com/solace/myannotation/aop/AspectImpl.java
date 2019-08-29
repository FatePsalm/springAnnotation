package com.solace.myannotation.aop;

import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.exception.ONSClientException;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionExecuter;
import com.aliyun.openservices.ons.api.transaction.TransactionProducer;
import com.aliyun.openservices.ons.api.transaction.TransactionStatus;
import com.solace.myannotation.config.rocketMq.MqConfig;
import javafx.util.Pair;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Date;

@Component
@Aspect
public class AspectImpl {
    @Autowired
    private MqConfig mqConfig;
    @Autowired
    private TransactionProducer transactionProducer;

    @Pointcut("@annotation(com.solace.myannotation.aop.Mu)")

    private void cut() {
    }

    // 开始环绕  
    //@Around("cut()")
    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println(1);
        // 首先获取方法的签名，joinPoint中有相应的签名信息
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        // 通过方法的签名可以获取方法本身
        Method method = signature.getMethod();
        // 获取方法的参数列表，注意此方法需JDK8+
        Parameter[] parameters = method.getParameters();
        // 获取参数列表的实际的值
        Object[] args = joinPoint.getArgs();
        // 建立参数列表和实际的值的对应关系
        Pair<Parameter, Object>[] parameterObjectPairs = new Pair[parameters.length];

    }

    @Before("cut()")
    public void before(JoinPoint joinPoint) {
        Mu mu=((MethodSignature)joinPoint.getSignature()).getMethod().getAnnotation(Mu.class);
        RequestMapping requestMapping=((MethodSignature)joinPoint.getSignature()).getMethod().getAnnotation(RequestMapping.class);
        System.out.println(mu.value());
        System.out.println(JSON.toJSONString(requestMapping));
        System.out.println("2");
        final Message message = new Message(mqConfig.getTopic(), mqConfig.getTag(), "mq send transaction message test".getBytes());
        try{
            SendResult sendResult = transactionProducer.send(message,new LocalTransactionExecuter() {
                @Override
                public TransactionStatus execute(Message message, Object o) {
                    return sendMsg();
                }
            } , null);
            assert sendResult != null;
        }catch (ONSClientException e){
            // 消息发送失败，需要进行重试处理，可重新发送这条消息或持久化这条数据进行补偿处理
            System.out.println(new Date() + " Send mq message failed! Topic is:" + mqConfig.getTopic());
            e.printStackTrace();
        }
    }
    public TransactionStatus sendMsg(){
        System.out.println("确认发送!");
        return TransactionStatus.CommitTransaction;
    }

   @After("cut()")
    public void after() {
        System.out.println("5");

    }
    //JoinPoint一定要出现在参数表的第一位
    @AfterReturning(value = "cut()",returning="result")
    public void logReturn(JoinPoint joinPoint,Object result){
        System.out.println(""+joinPoint.getSignature().getName()+"正常返回。。。@AfterReturning:运行结果：{"+result+"}");
    }
}