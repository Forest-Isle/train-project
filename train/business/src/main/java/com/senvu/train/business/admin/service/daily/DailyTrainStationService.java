package com.senvu.train.business.admin.service.daily;

import com.senvu.result.PageResult;
import com.senvu.result.Result;
import com.senvu.train.business.admin.pojo.dto.daily.DailyTrainStationDTO;
import com.senvu.train.business.admin.pojo.dto.query.daily.DailyTrainStationQueryDTO;

import java.time.LocalDate;

public interface DailyTrainStationService {
    PageResult list(DailyTrainStationQueryDTO dailyTrainStationQueryDTO);
    Result getById(Long id);
    Result save(DailyTrainStationDTO dailyTrainStationDTO);
    Result update(DailyTrainStationDTO dailyTrainStationDTO);
    Result delete(Long id);

    Result gen(LocalDate date);

}
