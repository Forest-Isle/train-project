package com.senvu.train.business.admin.pojo.dto.query;

import lombok.Data;

@Data
public class TrainStationQueryDTO {
    private Integer page;
    private Integer size;
    private String trainCode;
}


