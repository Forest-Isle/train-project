package com.senvu.train.business.admin.pojo.dto.query.daily;

import lombok.Data;

@Data
public class DailyTrainSeatQueryDTO {
    private Integer page;
    private Integer size;
    private String trainCode;
}


