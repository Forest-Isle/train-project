package com.senvu.train.member.service;

import com.senvu.common.TicketSaveDto;
import com.senvu.result.Result;

import java.util.List;

public interface TicketService {
    Result save(List<TicketSaveDto> ticketSaveDtos);
}
