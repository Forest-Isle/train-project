package com.senvu.train.business.admin.service.daily;

import com.senvu.result.PageResult;
import com.senvu.result.Result;
import com.senvu.train.business.admin.pojo.dto.daily.DailyTrainCarriageDTO;
import com.senvu.train.business.admin.pojo.dto.query.daily.DailyTrainCarriageQueryDTO;

import java.time.LocalDate;

public interface DailyTrainCarriageService {
    PageResult list(DailyTrainCarriageQueryDTO dailyTrainCarriageQueryDTO);
    Result getById(Long id);
    Result save(DailyTrainCarriageDTO dailyTrainCarriageDTO);
    Result update(DailyTrainCarriageDTO dailyTrainCarriageDTO);
    Result delete(Long id);

    void genCarriage(LocalDate date);
}
