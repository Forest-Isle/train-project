package com.senvu.train.business.pojo.vo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ConfirmOrderVO {
    private LocalDate date;
    private String tickets;
    private LocalDateTime createTime;
    private String start;
    private Long dailyTrainTicketId;
    private String trainCode;
    private String end;
    private LocalDateTime updateTime;
    private Long id;
    private Long memberId;
    private String status;
}
