package com.senvu.train.business.admin.service;

import com.senvu.result.PageResult;
import com.senvu.train.business.admin.pojo.dto.TrainSeatDTO;
import com.senvu.train.business.admin.pojo.dto.query.TrainSeatQueryDTO;
import com.senvu.train.business.admin.pojo.entity.TrainSeat;

public interface TrainSeatService {
    PageResult list(TrainSeatQueryDTO trainSeatQueryDTO);
    TrainSeat getById(Long id);
    void save(TrainSeatDTO trainSeatDTO);
    void update(TrainSeatDTO trainSeatDTO);
    void delete(Long id);
}
