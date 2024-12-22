package com.senvu.train.business.admin.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class TrainVO {
    private String startPinyin;
    private String code;
    private LocalDateTime createTime;
    private String start;
    private LocalTime startTime;
    private String end;
    private LocalDateTime updateTime;
    private Long id;
    private String endPinyin;
    private LocalTime endTime;
    private String type;
}
