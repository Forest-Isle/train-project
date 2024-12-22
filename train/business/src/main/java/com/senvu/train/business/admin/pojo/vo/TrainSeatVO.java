package com.senvu.train.business.admin.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TrainSeatVO {
    private Integer carriageIndex;
    private String col;
    private String seatType;
    private LocalDateTime createTime;
    private String trainCode;
    private LocalDateTime updateTime;
    private Long id;
    private String row;
    private Integer carriageSeatIndex;
}
