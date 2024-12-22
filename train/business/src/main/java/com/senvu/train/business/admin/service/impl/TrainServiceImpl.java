package com.senvu.train.business.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senvu.Enum.BusinessExceptionEnum;
import com.senvu.Enum.SeatColEnum;
import com.senvu.exception.BusinessException;
import com.senvu.result.PageResult;
import com.senvu.result.Result;
import com.senvu.train.business.admin.mapper.TrainCarriageMapper;
import com.senvu.train.business.admin.mapper.TrainMapper;
import com.senvu.train.business.admin.mapper.TrainSeatMapper;
import com.senvu.train.business.admin.mapper.TrainStationMapper;
import com.senvu.train.business.admin.pojo.dto.TrainDTO;
import com.senvu.train.business.admin.pojo.dto.query.TrainQueryDTO;
import com.senvu.train.business.admin.pojo.entity.Train;
import com.senvu.train.business.admin.pojo.entity.TrainCarriage;
import com.senvu.train.business.admin.pojo.entity.TrainSeat;
import com.senvu.train.business.admin.pojo.entity.TrainStation;
import com.senvu.train.business.admin.pojo.vo.TrainVO;
import com.senvu.train.business.admin.service.TrainService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainServiceImpl implements TrainService {

    @Resource
    private TrainMapper trainMapper;
    @Autowired
    private TrainSeatMapper trainSeatMapper;
    @Autowired
    private TrainCarriageMapper trainCarriageMapper;
    @Autowired
    private TrainStationMapper trainStationMapper;

    @Override
    public PageResult list(TrainQueryDTO trainQueryDTO) {
        Integer page = trainQueryDTO.getPage();
        Integer size = trainQueryDTO.getSize();
        Page<Train> pages = new Page<>(page,size);
        LambdaQueryWrapper<Train> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Train::getCode);
        trainMapper.selectPage(pages,wrapper);
        PageResult pageResult = new PageResult();
        pageResult.setTotal(pages.getTotal());
        pageResult.setList(pages.getRecords());
        return pageResult;
    }

    @Override
    public Train getById(Long id) {
        return trainMapper.selectById(id);
    }

    @Override
    public Result save(TrainDTO trainDTO) {
          Train train = new Train();
          BeanUtils.copyProperties(trainDTO,train);
          if (train.getId() != null){
              trainMapper.updateById(train);
          }else{
              if (isExisted(trainDTO)){
                  throw new BusinessException(BusinessExceptionEnum.TRAIN_EXITED_ERROR);
              }
              trainMapper.insert(train);
          }
          Result result = new Result();
          result.setCode(Result.SUCCESS_CODE);
          return result;
    }

    private Boolean isExisted(TrainDTO trainDTO) {
        LambdaQueryWrapper<Train> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Train::getCode,trainDTO.getCode());
        List<Train> trains = trainMapper.selectList(wrapper);
        return !trains.isEmpty();
    }

    @Override
    public void update(TrainDTO trainDTO) {
        Train train = new Train();
        BeanUtils.copyProperties(trainDTO,train);
     //   trainMapper.updateById();
    }

    @Override
    public void delete(Long id) {
        // 将车次删除 同时也将车站 车厢 车厢对应的座位删除
        LambdaQueryWrapper<TrainCarriage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TrainCarriage::getTrainCode,trainMapper.selectById(id).getCode());
        trainCarriageMapper.delete(wrapper);
        LambdaQueryWrapper<TrainSeat> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(TrainSeat::getTrainCode,trainMapper.selectById(id).getCode());
        trainSeatMapper.delete(wrapper1);
        LambdaQueryWrapper<TrainStation> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(TrainStation::getTrainCode,trainMapper.selectById(id).getCode());
        trainStationMapper.delete(wrapper2);
        trainMapper.deleteById(id);
    }

    @Override
    public List<TrainVO> all() {
        List<Train> trains = trainMapper.selectList(null);
        List<TrainVO> list = trains.stream().map(train -> {
            TrainVO trainVO = new TrainVO();
            BeanUtils.copyProperties(train, trainVO);
            return trainVO;
        }).collect(Collectors.toList());
        return list;
    }

    @Transactional
    @Override
    public void genSeat(String code) {
        // 1. 查询车次对应的所有车厢
        // 2. 删除所有车厢的原本设置的座位
        // 3. 对每一个车厢进行遍历 根据座位类型和座位数量生成座位
        LambdaQueryWrapper<TrainCarriage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TrainCarriage::getTrainCode,code);
        List<TrainCarriage> trainCarriages = trainCarriageMapper.selectList(wrapper); // 获取车厢
        for (TrainCarriage trainCarriage : trainCarriages) {
            LambdaQueryWrapper<TrainSeat> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.eq(TrainSeat::getCarriageIndex, trainCarriage.getIndex()).eq(TrainSeat::getTrainCode,trainCarriage.getTrainCode());
            trainSeatMapper.delete(wrapper1);// 车厢下的所有座位删除
        }

        for (TrainCarriage trainCarriage : trainCarriages) {

            String seatType = trainCarriage.getSeatType();
            Integer rowCount = trainCarriage.getRowCount();
            int seatIndex = 1;

            List<SeatColEnum> colsByType = SeatColEnum.getColsByType(trainCarriage.getSeatType());

            for (int i = 1; i <= rowCount; i++) {

                for (SeatColEnum seatTypeEnum : colsByType) {
                    TrainSeat trainSeat = new TrainSeat();
                    trainSeat.setCarriageIndex(trainCarriage.getIndex());
                    trainSeat.setCol(seatTypeEnum.getDesc());
                    trainSeat.setSeatType(seatType);
                    trainSeat.setTrainCode(trainCarriage.getTrainCode());
                    trainSeat.setRow(StrUtil.fillBefore(String.valueOf(i), '0', 2));
                    trainSeat.setCarriageSeatIndex(seatIndex++);

                    trainSeatMapper.insert(trainSeat);
                }

            }
        }
    }
}
