package com.senvu.train.business.admin.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class TrainStationVO {
    private LocalTime inTime;
    private Object km;
    private String namePinyin;
    private LocalDateTime createTime;
    private String name;
    private Integer index;
    private String trainCode;
    private LocalTime stopTime;
    private LocalDateTime updateTime;
    private Long id;
    private LocalTime outTime;
}
