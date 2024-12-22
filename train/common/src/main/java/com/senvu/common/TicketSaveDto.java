package com.senvu.common;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class TicketSaveDto {

    private String passengerId;
    private String PassengerIdCard;
    private String passengerName;
    private String passengerType;
    private String seat;
    private String seatTypeCode;
    private Integer carriageIndex;
    private String start;
    private String end;
    private String trainCode;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String col;
    private String row;
    private Long memberId;
}
