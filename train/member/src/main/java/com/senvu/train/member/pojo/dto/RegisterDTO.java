package com.senvu.train.member.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterDTO {

    @NotBlank(message = "手机号不能为空")
    private String mobile;
}
