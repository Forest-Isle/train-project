package com.senvu.train.business.admin.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TrainCarriageVO {
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
