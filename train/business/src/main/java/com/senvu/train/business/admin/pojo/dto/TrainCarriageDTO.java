package com.senvu.train.business.admin.pojo.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TrainCarriageDTO {
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
