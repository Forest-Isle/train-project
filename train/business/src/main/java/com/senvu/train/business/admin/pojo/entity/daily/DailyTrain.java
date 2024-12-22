package com.senvu.train.business.admin.pojo.entity.daily;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class DailyTrain {
    private LocalDate date;
    private String startPinyin;
    private String code;
    @TableField(value = "create_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime createTime;
    private String start;
    private LocalTime startTime;
    private String end;
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String endPinyin;
    private LocalTime endTime;
    private String type;
}
