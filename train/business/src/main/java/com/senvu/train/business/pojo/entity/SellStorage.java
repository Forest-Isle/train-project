package com.senvu.train.business.pojo.entity;

import lombok.Data;

@Data
public class SellStorage {

    private String sell;
    private String seatCol;
    private String seatRow;
    private Long dailyTrainSeatId;
    private String seatType;
    private Integer carriageIndex;

}
