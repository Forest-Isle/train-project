package com.senvu.train.business.admin.pojo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TrainCarriage {
    private String seatType;
    @TableField(value = "create_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime createTime;
    private Integer colCount;
    @TableField("`index`")
    private Integer index;
    private String trainCode;
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private Integer rowCount;
    private Integer seatCount;
}
