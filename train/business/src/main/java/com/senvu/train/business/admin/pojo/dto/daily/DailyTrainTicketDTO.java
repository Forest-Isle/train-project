package com.senvu.train.business.admin.pojo.dto.daily;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class DailyTrainTicketDTO {
    private LocalDate date;
    private String startPinyin;
    private Integer rw;
    private String start;
    private String trainCode;
    private LocalDateTime updateTime;
    private String endPinyin;
    private Integer ydz;
    private Integer startIndex;
    private BigDecimal ydzPrice;
    private BigDecimal ywPrice;
    private LocalDateTime createTime;
    private Integer endIndex;
    private BigDecimal rwPrice;
    private LocalTime startTime;
    private String end;
    private Integer edz;
    private Long id;
    private LocalTime endTime;
    private Integer yw;
    private BigDecimal edzPrice;
}
