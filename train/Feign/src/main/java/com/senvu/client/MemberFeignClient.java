package com.senvu.client;

import com.senvu.pojo.dto.TicketSaveDto;
import com.senvu.pojo.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("member")
public interface MemberFeignClient {

    @PostMapping("member/ticket")
    Result save(@RequestBody List<TicketSaveDto> ticketSaveDtos);
}
