package com.senvu.train.business.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senvu.Enum.BusinessExceptionEnum;
import com.senvu.exception.BusinessException;
import com.senvu.result.PageResult;
import com.senvu.result.Result;
import com.senvu.train.business.admin.mapper.TrainStationMapper;
import com.senvu.train.business.admin.pojo.dto.TrainStationDTO;
import com.senvu.train.business.admin.pojo.dto.query.TrainStationQueryDTO;
import com.senvu.train.business.admin.pojo.entity.TrainStation;
import com.senvu.train.business.admin.service.TrainStationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainStationServiceImpl implements TrainStationService {

    @Autowired
    private TrainStationMapper trainStationMapper;

    @Override
    public PageResult list(TrainStationQueryDTO trainStationQueryDTO) {
        Integer page = trainStationQueryDTO.getPage();
        Integer size = trainStationQueryDTO.getSize();
        Page<TrainStation> pages = new Page<>(page,size);
        LambdaQueryWrapper<TrainStation> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(TrainStation::getTrainCode).orderByAsc(TrainStation::getIndex);
        if (trainStationQueryDTO.getTrainCode() != null){
            wrapper.eq(TrainStation::getTrainCode,trainStationQueryDTO.getTrainCode());
        }
        trainStationMapper.selectPage(pages,wrapper);
        PageResult pageResult = new PageResult();
        pageResult.setTotal(pages.getTotal());
        pageResult.setList(pages.getRecords());
        return pageResult;
    }

    @Override
    public TrainStation getById(Long id) {
        return trainStationMapper.selectById(id);
    }

    @Override
    public Result save(TrainStationDTO trainStationDTO) {
          TrainStation trainStation = new TrainStation();
          BeanUtils.copyProperties(trainStationDTO,trainStation);
          if (trainStationDTO.getId() != null){
              trainStationMapper.updateById(trainStation);
          } else {
              if (isExisted(trainStationDTO)){
                  throw new BusinessException(BusinessExceptionEnum.TRAIN_STATION_EXITED_ERROR);
              }
              trainStationMapper.insert(trainStation);
          }
          Result result = new Result();
          result.setCode(Result.SUCCESS_CODE);
          return result;
    }

    private Boolean isExisted(TrainStationDTO trainStationDTO) {
        LambdaQueryWrapper<TrainStation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TrainStation::getTrainCode,trainStationDTO.getTrainCode()).eq(TrainStation::getIndex,trainStationDTO.getIndex());
        List<TrainStation> trainStations = trainStationMapper.selectList(wrapper);

        LambdaQueryWrapper<TrainStation> wrapper1 = new LambdaQueryWrapper<>();
        wrapper.eq(TrainStation::getTrainCode,trainStationDTO.getTrainCode()).eq(TrainStation::getName,trainStationDTO.getName());
        List<TrainStation> trainStations1 = trainStationMapper.selectList(wrapper);

        return !trainStations.isEmpty() || !trainStations1.isEmpty();
    }

    @Override
    public void update(TrainStationDTO trainStationDTO) {
        TrainStation trainStation = new TrainStation();
        BeanUtils.copyProperties(trainStationDTO,trainStation);
     //   trainStationMapper.updateById();
    }

    @Override
    public void delete(Long id) {
        trainStationMapper.deleteById(id);
    }

    @Override
    public Result all() {
        List<TrainStation> trainStations = trainStationMapper.selectList(null);
        Result result = new Result();
        result.setCode(Result.SUCCESS_CODE);
        result.setData(trainStations);
        return result;
    }
}
