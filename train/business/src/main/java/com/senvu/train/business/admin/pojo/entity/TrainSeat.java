package com.senvu.train.business.admin.pojo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TrainSeat {
    private Integer carriageIndex;
    private String col;
    private String seatType;
    @TableField(value = "create_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime createTime;
    private String trainCode;
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    @TableField("`row`")
    private String row;
    private Integer carriageSeatIndex;
}
