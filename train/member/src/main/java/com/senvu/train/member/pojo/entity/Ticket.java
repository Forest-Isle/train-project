package com.senvu.train.member.pojo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class Ticket {
    private Integer carriageIndex;
    private String passengerName;
    private String endStation;
    private String seatRow;
    private String seatCol;
    private String trainCode;
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    private String seatType;
    @TableField(value = "create_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime createTime;
    private LocalDate trainDate;
    private Long passengerId;
    private String startStation;
    private LocalTime startTime;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private LocalTime endTime;
    private Long memberId;
}
