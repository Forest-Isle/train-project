package com.senvu.train.business.admin.pojo.dto.daily;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class DailyTrainSeatDTO {
    private LocalDate date;
    private Integer carriageIndex;
    private String col;
    private String seatType;
    private LocalDateTime createTime;
    private String sell;
    private String trainCode;
    private LocalDateTime updateTime;
    private Long id;
    private String row;
    private Integer carriageSeatIndex;
}
