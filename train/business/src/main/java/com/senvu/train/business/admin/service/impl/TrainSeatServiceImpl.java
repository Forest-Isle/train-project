package com.senvu.train.business.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senvu.train.business.admin.mapper.TrainSeatMapper;
import com.senvu.train.business.admin.pojo.dto.TrainSeatDTO;
import com.senvu.train.business.admin.pojo.dto.query.TrainSeatQueryDTO;
import com.senvu.train.business.admin.pojo.entity.TrainSeat;
import com.senvu.train.business.admin.service.TrainSeatService;
import org.springframework.beans.BeanUtils;
import com.senvu.result.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainSeatServiceImpl implements TrainSeatService {

    @Autowired
    private TrainSeatMapper trainSeatMapper;

    @Override
    public PageResult list(TrainSeatQueryDTO trainSeatQueryDTO) {
        Integer page = trainSeatQueryDTO.getPage();
        Integer size = trainSeatQueryDTO.getSize();
        Page<TrainSeat> pages = new Page<>(page,size);
        LambdaQueryWrapper<TrainSeat> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(TrainSeat::getTrainCode).orderByAsc(TrainSeat::getCarriageIndex).orderByAsc(TrainSeat::getCarriageSeatIndex);
        if (trainSeatQueryDTO.getTrainCode() != null){
            wrapper.eq(TrainSeat::getTrainCode,trainSeatQueryDTO.getTrainCode());
        }
        trainSeatMapper.selectPage(pages,wrapper);
        PageResult pageResult = new PageResult();
        pageResult.setTotal(pages.getTotal());
        pageResult.setList(pages.getRecords());
        return pageResult;
    }

    @Override
    public TrainSeat getById(Long id) {
        return trainSeatMapper.selectById(id);
    }

    @Override
    public void save(TrainSeatDTO trainSeatDTO) {
          TrainSeat trainSeat = new TrainSeat();
          BeanUtils.copyProperties(trainSeatDTO,trainSeat);
          if (trainSeatDTO.getId() != null){
              trainSeatMapper.updateById(trainSeat);
          } else {
            trainSeatMapper.insert(trainSeat);
          }
    }

    @Override
    public void update(TrainSeatDTO trainSeatDTO) {
        TrainSeat trainSeat = new TrainSeat();
        BeanUtils.copyProperties(trainSeatDTO,trainSeat);
     //   trainSeatMapper.updateById();
    }

    @Override
    public void delete(Long id) {
        trainSeatMapper.deleteById(id);
    }
}
