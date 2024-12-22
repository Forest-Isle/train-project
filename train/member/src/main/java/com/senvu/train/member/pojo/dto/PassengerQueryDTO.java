package com.senvu.train.member.pojo.dto;

import lombok.Data;

@Data
public class PassengerQueryDTO {

    private Long memberId;

    private Integer page;

    private Integer size;

}