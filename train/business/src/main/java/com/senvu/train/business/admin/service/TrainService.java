package com.senvu.train.business.admin.service;

import com.senvu.result.PageResult;
import com.senvu.result.Result;
import com.senvu.train.business.admin.pojo.dto.TrainDTO;
import com.senvu.train.business.admin.pojo.dto.query.TrainQueryDTO;
import com.senvu.train.business.admin.pojo.entity.Train;
import com.senvu.train.business.admin.pojo.vo.TrainVO;

import java.util.List;

public interface TrainService {
    PageResult list(TrainQueryDTO trainQueryDTO);
    Train getById(Long id);
    Result save(TrainDTO trainDTO);
    void update(TrainDTO trainDTO);
    void delete(Long id);

    List<TrainVO> all();

    void genSeat(String code);
}
