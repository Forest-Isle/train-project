package com.senvu.train.member.controller;

import com.senvu.context.LoginMemberContext;
import com.senvu.result.PageResult;
import com.senvu.result.Result;
import com.senvu.train.member.pojo.dto.*;
import com.senvu.train.member.service.PassengerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("passenger")
public class PassengerController {

    @Autowired
    private PassengerService passengerService;

    Logger LOG = LoggerFactory.getLogger(PassengerController.class);


    @PostMapping("save")
    public Result register(@RequestBody @Valid PassengerSaveDTO passengerSaveDTO){
        return passengerService.save(passengerSaveDTO);
    }

    @GetMapping("query-list")
    public PageResult list(@Valid PassengerQueryDTO passengerQueryDTO){
        passengerQueryDTO.setMemberId(LoginMemberContext.getMemberId());
        return passengerService.list(passengerQueryDTO);
    }

    @DeleteMapping("delete/{id}")
    public Result delete(@PathVariable Long id){
        return passengerService.delete(id);
    }

    @GetMapping("query-mine")
    public Result queryMine(){
        return passengerService.queryMine();
    }
}
