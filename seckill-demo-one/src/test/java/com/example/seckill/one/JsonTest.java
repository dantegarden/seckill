package com.example.seckill.one;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.seckill.one.model.entity.User;
import com.example.seckill.one.utils.FastjsonUtils;
import org.junit.Test;

import java.io.Serializable;
import java.util.Date;

public class JsonTest {

    @Test
    public void test(){
        User user = new User();
        user.setId(1L).setLoginName("lij").setRegisterDate(new Date()).setMobile("19216813213").setNickname("lij").setLoginCount(1).setPassword("408a54222d9e7c15607877269ba45e3a").setSalt("abcdef");
        String json = FastjsonUtils.convertObjectToJSON(user);
//        SerializerFeature[] features = {
//                SerializerFeature.WriteMapNullValue, // 输出空置字段
//                SerializerFeature.WriteNullListAsEmpty, // list字段如果为null，输出为[]，而不是null
//                SerializerFeature.WriteNullNumberAsZero, // 数值字段如果为null，输出为0，而不是null
//                SerializerFeature.WriteNullBooleanAsFalse, // Boolean字段如果为null，输出为false，而不是null
//                SerializerFeature.WriteNullStringAsEmpty, // 字符类型字段如果为null，输出为""，而不是null
//                SerializerFeature.WriteDateUseDateFormat //日期类型用日期字符串
//        };
//        String json2 = JSON.toJSONString(user,features);
        System.out.println(json);
//        System.out.println(json2);
        User user2 = (User) FastjsonUtils.convertJsonToObject(json, User.class);
        System.out.println(user2);
    }
}
