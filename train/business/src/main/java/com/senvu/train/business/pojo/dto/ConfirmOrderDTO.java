package com.senvu.train.business.pojo.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ConfirmOrderDTO {
    private LocalDate date;
    private List<TicketDTO> tickets;
    private String start;
    private Long dailyTrainTicketId;
    private String trainCode;
    private String end;
    private String status;
    private String imageCodeToken;
    private String imageCode;
    private Integer lineNumber;
}
