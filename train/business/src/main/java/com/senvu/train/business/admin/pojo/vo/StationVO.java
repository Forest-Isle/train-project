package com.senvu.train.business.admin.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StationVO {
    private String name_pinyin;
    private LocalDateTime update_time;
    private LocalDateTime create_time;
    private String name;
    private Long id;
    private String name_py;
}
