package com.senvu.train.business.admin.service.impl.daily;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senvu.Enum.BusinessExceptionEnum;
import com.senvu.exception.BusinessException;
import com.senvu.result.PageResult;
import com.senvu.result.Result;
import com.senvu.train.business.admin.mapper.TrainMapper;
import com.senvu.train.business.admin.mapper.daily.DailyTrainMapper;
import com.senvu.train.business.admin.pojo.dto.daily.DailyTrainDTO;
import com.senvu.train.business.admin.pojo.dto.query.daily.DailyTrainQueryDTO;
import com.senvu.train.business.admin.pojo.entity.Train;
import com.senvu.train.business.admin.pojo.entity.daily.DailyTrain;
import com.senvu.train.business.admin.service.daily.DailyTrainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class DailyTrainServiceImpl implements DailyTrainService {

    @Autowired
    private DailyTrainMapper dailyTrainMapper;
    @Autowired
    private TrainMapper trainMapper;

    @Override
    public PageResult list(DailyTrainQueryDTO dailyTrainQueryDTO) {
        Integer page = dailyTrainQueryDTO.getPage();
        Integer size = dailyTrainQueryDTO.getSize();
        Page<DailyTrain> pages = new Page<>(page,size);
        LambdaQueryWrapper<DailyTrain> wrapper = new LambdaQueryWrapper<>();
        deal(dailyTrainQueryDTO, wrapper);
        dailyTrainMapper.selectPage(pages,wrapper);
        PageResult pageResult = new PageResult();
        pageResult.setTotal(pages.getTotal());
        pageResult.setList(pages.getRecords());
        return pageResult;
    }

    private static void deal(DailyTrainQueryDTO dailyTrainQueryDTO, LambdaQueryWrapper<DailyTrain> wrapper) {
        wrapper.orderByDesc(DailyTrain::getDate).orderByAsc(DailyTrain::getCode);
        if (dailyTrainQueryDTO.getDate() != null && dailyTrainQueryDTO.getCode() != null){
            wrapper.eq(DailyTrain::getDate, dailyTrainQueryDTO.getDate()).eq(DailyTrain::getCode, dailyTrainQueryDTO.getCode());
        } else if (dailyTrainQueryDTO.getDate() != null) {
            wrapper.eq(DailyTrain::getDate, dailyTrainQueryDTO.getDate());
        } else if (dailyTrainQueryDTO.getCode() != null) {
            wrapper.eq(DailyTrain::getCode, dailyTrainQueryDTO.getCode());
        }
    }

    @Override
    public DailyTrain getById(Long id) {
        return dailyTrainMapper.selectById(id);
    }

    @Override
    public Result save(DailyTrainDTO dailyTrainDTO) {
          DailyTrain dailyTrain = new DailyTrain();
          BeanUtils.copyProperties(dailyTrainDTO,dailyTrain);
          if (dailyTrain.getId() != null){
              dailyTrainMapper.updateById(dailyTrain);
          } else {
              if (existed(dailyTrain)) {
                  throw new BusinessException(BusinessExceptionEnum.DAILY_TRAIN_EXITED_ERROR);
              }
              dailyTrainMapper.insert(dailyTrain);
          }
          return new Result(Result.SUCCESS_CODE);
    }

    private boolean existed(DailyTrain dailyTrainCarriage) {
        LambdaQueryWrapper<DailyTrain> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DailyTrain::getCode, dailyTrainCarriage.getCode()).eq(DailyTrain::getDate, dailyTrainCarriage.getDate());
        return !dailyTrainMapper.selectList(wrapper).isEmpty();
    }

    @Override
    public Result update(DailyTrainDTO dailyTrainDTO) {
        DailyTrain dailyTrain = new DailyTrain();
        BeanUtils.copyProperties(dailyTrainDTO,dailyTrain);
     //   dailyTrainMapper.updateById();
        return new Result(Result.SUCCESS_CODE);
    }

    @Override
    public Result delete(Long id) {
        dailyTrainMapper.deleteById(id);
        return new Result(Result.SUCCESS_CODE);
    }

    @Override
    public void gen(LocalDate date) {
        // 1. 查询车次信息
        List<Train> trains = trainMapper.selectList(null);
        if (trains.isEmpty()) {
            log.info("无车次数据");
            return;
        }
        for (Train train : trains) {
            // 2. 删除车次对应的当日车次数据
            LambdaQueryWrapper<DailyTrain> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(DailyTrain::getCode, train.getCode());
            dailyTrainMapper.delete(wrapper);
            // 3. 重新生成的当日日车次数据
            DailyTrain dailyTrain = new DailyTrain();
            BeanUtils.copyProperties(train, dailyTrain);
            dailyTrain.setDate(date);
            dailyTrainMapper.insert(dailyTrain);
        }
    }
}
