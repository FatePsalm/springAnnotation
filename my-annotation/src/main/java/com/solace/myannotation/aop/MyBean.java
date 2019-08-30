package com.solace.myannotation.aop;

import java.lang.reflect.Field;

public class MyBean {
    @MyAnnotation(20)
    private int value;
    @Override
    public String toString() {
        return String.valueOf(value);
    }
    public static void main(String[] args) {
        try {
            Field field = MyBean.class.getDeclaredField("value");//获取成员变量value
            field.setAccessible(true);//将value设置成可访问的
            if(field.isAnnotationPresent(MyAnnotation.class)){//判断成员变量是否有注解
                MyAnnotation myAnnotation = field.getAnnotation(MyAnnotation.class);//获取定义在成员变量中的注解MyAnnotation
                int value = myAnnotation.value();//获取定义在MyBean的MyAnnotation里面属性值
                MyBean myBean=new MyBean();
                field.setInt(myBean, value);//将注解的值20可以赋给成员变量value
                System.out.println(myBean);//验证结果
            }
        } catch (Exception e) {
            e.printStackTrace();
        };
    }
}