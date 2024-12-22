package com.senvu.train.business.admin.service.daily;

import com.senvu.result.PageResult;
import com.senvu.result.Result;
import com.senvu.train.business.admin.pojo.dto.daily.DailyTrainTicketDTO;
import com.senvu.train.business.admin.pojo.dto.query.daily.DailyTrainTicketQueryDTO;

public interface DailyTrainTicketService {
    PageResult list(DailyTrainTicketQueryDTO dailyTrainTicketQueryDTO);
    Result getById(Long id);
    Result save(DailyTrainTicketDTO dailyTrainTicketDTO);
    Result update(DailyTrainTicketDTO dailyTrainTicketDTO);
    Result delete(Long id);
}
