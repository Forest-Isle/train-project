package com.senvu.train.member.service.impl;

import com.senvu.common.TicketSaveDto;
import com.senvu.result.Result;
import com.senvu.train.member.mapper.TicketMapper;
import com.senvu.train.member.pojo.entity.Ticket;
import com.senvu.train.member.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketMapper ticketMapper;

    @Override
    public Result save(List<TicketSaveDto> ticketSaveDtos) {

        for (TicketSaveDto ticketSaveDto : ticketSaveDtos) {
            Ticket ticket = new Ticket();
            ticket.setCarriageIndex(ticketSaveDto.getCarriageIndex());
            ticket.setPassengerName(ticketSaveDto.getPassengerName());
            ticket.setEndStation(ticketSaveDto.getEnd());
            ticket.setSeatRow(ticketSaveDto.getRow());
            ticket.setSeatCol(ticketSaveDto.getCol());
            ticket.setTrainCode(ticketSaveDto.getTrainCode());
            ticket.setSeatType(ticketSaveDto.getSeatTypeCode());
            ticket.setTrainDate(ticketSaveDto.getDate());
            ticket.setPassengerId(Long.valueOf(ticketSaveDto.getPassengerId()));
            ticket.setStartStation(ticketSaveDto.getStart());
            ticket.setStartTime(ticketSaveDto.getStartTime());
            ticket.setEndTime(ticketSaveDto.getEndTime());
            ticket.setMemberId(ticketSaveDto.getMemberId());

            ticketMapper.insert(ticket);
        }
            return new Result<>(Result.SUCCESS_CODE);
    }
}
