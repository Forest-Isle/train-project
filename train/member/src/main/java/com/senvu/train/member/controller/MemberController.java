package com.senvu.train.member.controller;

import com.senvu.result.Result;
import com.senvu.train.member.pojo.dto.LoginDTO;
import com.senvu.train.member.pojo.dto.RegisterDTO;
import com.senvu.train.member.pojo.dto.SendCodeDTO;
import com.senvu.train.member.service.MemberService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {

    @Autowired
    private MemberService memberService;

    Logger LOG = LoggerFactory.getLogger(MemberController.class);

//    @GetMapping("member")
//    public String hello(){
//        return memberService.getCount() + "";
//    }

    @PostMapping("register")
    public Result register(@RequestBody @Valid RegisterDTO registerDTO){
        Result register = memberService.register(registerDTO);
        return register;
    }

    @PostMapping("sendCode")
    public Result sendCode(@RequestBody @Valid SendCodeDTO sendCodeDTO){
        LOG.info("接收到前端发送验证码请求");
        return memberService.sendCode(sendCodeDTO);
    }

    @PostMapping("login")
    public Result login(@RequestBody @Valid LoginDTO loginDTO){
        LOG.info("接收到前端登录请求");
        return memberService.login(loginDTO);
    }
}
