package com.senvu.train.business.admin.pojo.dto.daily;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class DailyTrainStationDTO {
    private LocalDate date;
    private LocalTime inTime;
    private BigDecimal km;
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
