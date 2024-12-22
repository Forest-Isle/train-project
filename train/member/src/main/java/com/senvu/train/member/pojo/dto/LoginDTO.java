package com.senvu.train.member.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {

    @NotBlank(message = "手机号不能为空")
    private String mobile;
    @NotBlank(message = "短信验证码不能为空")
    private String code;
}
