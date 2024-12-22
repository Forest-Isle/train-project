package com.senvu.train.business.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senvu.Enum.BusinessExceptionEnum;
import com.senvu.Enum.SeatColEnum;
import com.senvu.exception.BusinessException;
import com.senvu.result.PageResult;
import com.senvu.result.Result;
import com.senvu.train.business.admin.mapper.TrainCarriageMapper;
import com.senvu.train.business.admin.mapper.TrainSeatMapper;
import com.senvu.train.business.admin.pojo.dto.TrainCarriageDTO;
import com.senvu.train.business.admin.pojo.dto.query.TrainCarriageQueryDTO;
import com.senvu.train.business.admin.pojo.entity.TrainCarriage;
import com.senvu.train.business.admin.pojo.entity.TrainSeat;
import com.senvu.train.business.admin.pojo.vo.TrainCarriageVO;
import com.senvu.train.business.admin.service.TrainCarriageService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainCarriageServiceImpl implements TrainCarriageService {

    @Autowired
    private TrainCarriageMapper trainCarriageMapper;
    @Autowired
    private TrainSeatMapper trainSeatMapper;

    @Override
    public PageResult list(TrainCarriageQueryDTO trainCarriageQueryDTO) {
        Integer page = trainCarriageQueryDTO.getPage();
        Integer size = trainCarriageQueryDTO.getSize();
        Page<TrainCarriage> pages = new Page<>(page,size);
        LambdaQueryWrapper<TrainCarriage> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(TrainCarriage::getTrainCode).orderByAsc(TrainCarriage::getIndex);
        if (trainCarriageQueryDTO.getTrainCode() != null){
            wrapper.eq(TrainCarriage::getTrainCode,trainCarriageQueryDTO.getTrainCode());
        }
        trainCarriageMapper.selectPage(pages,wrapper);
        PageResult pageResult = new PageResult();
        pageResult.setTotal(pages.getTotal());
        pageResult.setList(pages.getRecords());
        return pageResult;
    }

    @Override
    public TrainCarriage getById(Long id) {
        return trainCarriageMapper.selectById(id);
    }

    @Override
    public Result save(TrainCarriageDTO trainCarriageDTO) {
          TrainCarriage trainCarriage = new TrainCarriage();
          BeanUtils.copyProperties(trainCarriageDTO,trainCarriage);
          if (trainCarriageDTO.getId() != null){
              Integer row = trainCarriage.getRowCount();
              List<SeatColEnum> colsByType = SeatColEnum.getColsByType(trainCarriage.getSeatType());
              int col = colsByType.size();
              trainCarriage.setSeatCount(row * col);
              trainCarriageMapper.updateById(trainCarriage);
          } else {
              if (isExisted(trainCarriageDTO)){
                  throw new BusinessException(BusinessExceptionEnum.CARRIAGE_EXITED_ERROR);
              }
              List<SeatColEnum> colsByType = SeatColEnum.getColsByType(trainCarriageDTO.getSeatType());
              trainCarriage.setColCount(colsByType.size());
              trainCarriage.setSeatCount(colsByType.size() * trainCarriage.getRowCount());
              trainCarriageMapper.insert(trainCarriage);
          }
          Result result = new Result();
          result.setCode(Result.SUCCESS_CODE);
          return result;
    }

    private Boolean isExisted(TrainCarriageDTO trainCarriageDTO) {
        LambdaQueryWrapper<TrainCarriage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TrainCarriage::getTrainCode,trainCarriageDTO.getTrainCode()).eq(TrainCarriage::getIndex,trainCarriageDTO.getIndex());
        List<TrainCarriage> trainCarriages = trainCarriageMapper.selectList(wrapper);
        return !trainCarriages.isEmpty();
    }

    @Override
    public void update(TrainCarriageDTO trainCarriageDTO) {
        TrainCarriage trainCarriage = new TrainCarriage();
        BeanUtils.copyProperties(trainCarriageDTO,trainCarriage);
     //   trainCarriageMapper.updateById();
    }

    @Override
    public void delete(Long id) {
        // 删除车厢 同时将车厢的所有座位删除
        LambdaQueryWrapper<TrainSeat> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TrainSeat::getCarriageIndex,trainCarriageMapper.selectById(id).getIndex()).eq(TrainSeat::getTrainCode,trainCarriageMapper.selectById(id).getTrainCode());
        trainSeatMapper.delete(wrapper);
        trainCarriageMapper.deleteById(id);
    }

    @Override
    public List<TrainCarriageVO> all() {
        List<TrainCarriage> trainCarriages = trainCarriageMapper.selectList(null);
        List<TrainCarriageVO> list = trainCarriages.stream().map(
                trainCarriage -> {
                    TrainCarriageVO trainCarriageVO = new TrainCarriageVO();
                    BeanUtils.copyProperties(trainCarriage, trainCarriageVO);
                    return trainCarriageVO;
                }
        ).collect(Collectors.toList());
        return list;
    }
}
