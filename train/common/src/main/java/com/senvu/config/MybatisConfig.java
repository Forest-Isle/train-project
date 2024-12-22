package com.senvu.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MybatisConfig implements MetaObjectHandler {


    @Override
    public void insertFill(MetaObject metaObject) {
        //设置insert操作时的时间点
        metaObject.setValue("createTime", LocalDateTime.now());
        //设置update操作时的时间点
        metaObject.setValue("updateTime",LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        //设置update操作时的时间点
        metaObject.setValue("updateTime",LocalDateTime.now());
    }
}
