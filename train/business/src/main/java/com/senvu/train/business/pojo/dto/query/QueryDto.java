package com.senvu.train.business.pojo.dto.query;

import lombok.Data;

import java.time.LocalDate;

@Data
public class QueryDto {

    private Integer page;
    private Integer size;
    private String trainCode;
    private LocalDate date;
    private String start;
    private String end;

}
