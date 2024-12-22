package com.senvu.train.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.senvu.train.business.pojo.entity.ConfirmOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ConfirmOrderMapper extends BaseMapper<ConfirmOrder>{

    void insertConfirmOrder(@Param("confirmOrder") ConfirmOrder confirmOrder,@Param("json") String json);

    void updateConfirmOrderById(ConfirmOrder confirmOrder);
}
