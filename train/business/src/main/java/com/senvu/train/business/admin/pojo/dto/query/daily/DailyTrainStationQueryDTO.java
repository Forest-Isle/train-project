package com.senvu.train.business.admin.pojo.dto.query.daily;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DailyTrainStationQueryDTO {
    private Integer page;
    private Integer size;
    private String trainCode;
    private LocalDate date;
}


