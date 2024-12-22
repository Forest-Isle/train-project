package com.senvu.train.business.admin.service;

import com.senvu.result.PageResult;
import com.senvu.result.Result;
import com.senvu.train.business.admin.pojo.dto.TrainCarriageDTO;
import com.senvu.train.business.admin.pojo.dto.query.TrainCarriageQueryDTO;
import com.senvu.train.business.admin.pojo.entity.TrainCarriage;
import com.senvu.train.business.admin.pojo.vo.TrainCarriageVO;

import java.util.List;

public interface TrainCarriageService {
    PageResult list(TrainCarriageQueryDTO trainCarriageQueryDTO);
    TrainCarriage getById(Long id);
    Result save(TrainCarriageDTO trainCarriageDTO);
    void update(TrainCarriageDTO trainCarriageDTO);
    void delete(Long id);

    List<TrainCarriageVO> all();
}
