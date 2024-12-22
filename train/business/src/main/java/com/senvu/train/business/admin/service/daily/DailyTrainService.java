package com.senvu.train.business.admin.service.daily;

import com.senvu.result.PageResult;
import com.senvu.result.Result;
import com.senvu.train.business.admin.pojo.dto.daily.DailyTrainDTO;
import com.senvu.train.business.admin.pojo.dto.query.daily.DailyTrainQueryDTO;
import com.senvu.train.business.admin.pojo.entity.daily.DailyTrain;

import java.time.LocalDate;

public interface DailyTrainService {
    PageResult list(DailyTrainQueryDTO dailyTrainQueryDTO);
    DailyTrain getById(Long id);
    Result save(DailyTrainDTO dailyTrainDTO);
    Result update(DailyTrainDTO dailyTrainDTO);
    Result delete(Long id);

    void gen(LocalDate date);
}
