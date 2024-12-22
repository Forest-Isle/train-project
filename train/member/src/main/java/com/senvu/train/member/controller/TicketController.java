package com.senvu.train.member.controller;

import com.senvu.common.TicketSaveDto;
import com.senvu.result.Result;
import com.senvu.train.member.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping
    public Result save(@RequestBody List<TicketSaveDto> ticketSaveDtos) {
        return ticketService.save(ticketSaveDtos);
    }
}
