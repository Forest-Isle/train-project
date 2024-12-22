package com.senvu.train.business.admin.service;

import com.senvu.result.PageResult;
import com.senvu.result.Result;
import com.senvu.train.business.admin.pojo.dto.StationDTO;
import com.senvu.train.business.admin.pojo.dto.query.StationQueryDTO;
import com.senvu.train.business.admin.pojo.entity.Station;

public interface StationService {
    PageResult list(StationQueryDTO stationQueryDTO);
    Station getById(Long id);
    Result save(StationDTO stationDTO);
    void update(StationDTO stationDTO);
    void delete(Long id);

    Result all();
}
