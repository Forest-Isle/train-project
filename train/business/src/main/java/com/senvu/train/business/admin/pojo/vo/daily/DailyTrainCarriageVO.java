package com.senvu.train.business.admin.pojo.vo.daily;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class DailyTrainCarriageVO {
    private LocalDate date;
    private String seatType;
    private LocalDateTime createTime;
    private Integer colCount;
    private Integer index;
    private String trainCode;
    private LocalDateTime updateTime;
    private Long id;
    private Integer rowCount;
    private Integer seatCount;
}
