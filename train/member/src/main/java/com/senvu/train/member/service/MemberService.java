package com.senvu.train.member.service;

import com.senvu.result.Result;
import com.senvu.train.member.pojo.dto.LoginDTO;
import com.senvu.train.member.pojo.dto.RegisterDTO;
import com.senvu.train.member.pojo.dto.SendCodeDTO;
import jakarta.validation.Valid;

public interface MemberService {
//    Integer getCount();

    Result register(RegisterDTO registerDTO);

    Result sendCode(@Valid SendCodeDTO sendCodeDTO);

    Result login(LoginDTO loginDTO);
}
