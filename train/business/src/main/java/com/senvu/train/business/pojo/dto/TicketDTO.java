package com.senvu.train.business.pojo.dto;

import lombok.Data;

@Data
public class TicketDTO {

    private String passengerId;
    private String PassengerIdCard;
    private String passengerName;
    private String passengerType;
    private String seat;
    private String seatTypeCode;

}
