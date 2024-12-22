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
public class DailyTrainTicket {
    private LocalDate date;
    private String startPinyin;
    private Integer rw;
    private String start;
    private String trainCode;
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    private String endPinyin;
    private Integer ydz;
    private Integer startIndex;
    private BigDecimal ydzPrice;
    private BigDecimal ywPrice;
    @TableField(value = "create_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime createTime;
    private Integer endIndex;
    private BigDecimal rwPrice;
    private LocalTime startTime;
    private String end;
    private Integer edz;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private LocalTime endTime;
    private Integer yw;
    private BigDecimal edzPrice;
}
