package com.senvu.train.business.admin.pojo.entity.daily;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class DailyTrainStation {
    private LocalDate date;
    private LocalTime inTime;
    private BigDecimal km;
    private String namePinyin;
    @TableField(value = "create_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime createTime;
    private String name;
    @TableField("`index`")
    private Integer index;
    private String trainCode;
    private LocalTime stopTime;
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private LocalTime outTime;
}
