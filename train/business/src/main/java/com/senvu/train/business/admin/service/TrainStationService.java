package com.senvu.train.business.admin.service;

import com.senvu.result.PageResult;
import com.senvu.result.Result;
import com.senvu.train.business.admin.pojo.dto.TrainStationDTO;
import com.senvu.train.business.admin.pojo.dto.query.TrainStationQueryDTO;
import com.senvu.train.business.admin.pojo.entity.TrainStation;

public interface TrainStationService {
    PageResult list(TrainStationQueryDTO trainStationQueryDTO);
    TrainStation getById(Long id);
    Result save(TrainStationDTO trainStationDTO);
    void update(TrainStationDTO trainStationDTO);
    void delete(Long id);

    Result all();

}
