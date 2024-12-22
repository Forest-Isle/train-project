package com.senvu.train.business.admin.service.daily;

import com.senvu.result.PageResult;
import com.senvu.result.Result;
import com.senvu.train.business.admin.pojo.dto.daily.DailyTrainSeatDTO;
import com.senvu.train.business.admin.pojo.dto.query.daily.DailyTrainSeatQueryDTO;

import java.time.LocalDate;

public interface DailyTrainSeatService {

    PageResult list(DailyTrainSeatQueryDTO dailyTrainSeatQueryDTO);
    Result getById(Long id);
    Result save(DailyTrainSeatDTO dailyTrainSeatDTO);
    Result update(DailyTrainSeatDTO dailyTrainSeatDTO);
    Result delete(Long id);

    void genSeat(LocalDate date);
}
