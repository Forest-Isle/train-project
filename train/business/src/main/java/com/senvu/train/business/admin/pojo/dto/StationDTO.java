package com.senvu.train.business.admin.pojo.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StationDTO {

    private Long id;
    private String namePinyin;
    private String name;
    private String namePy;
    private LocalDateTime updateTime;
    private LocalDateTime createTime;
}
