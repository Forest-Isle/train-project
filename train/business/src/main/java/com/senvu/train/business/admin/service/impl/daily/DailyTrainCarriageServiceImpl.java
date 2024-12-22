package com.senvu.train.business.admin.service.impl.daily;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senvu.Enum.BusinessExceptionEnum;
import com.senvu.Enum.SeatColEnum;
import com.senvu.exception.BusinessException;
import com.senvu.result.PageResult;
import com.senvu.result.Result;
import com.senvu.train.business.admin.mapper.TrainCarriageMapper;
import com.senvu.train.business.admin.mapper.daily.DailyTrainCarriageMapper;
import com.senvu.train.business.admin.mapper.daily.DailyTrainMapper;
import com.senvu.train.business.admin.pojo.dto.daily.DailyTrainCarriageDTO;
import com.senvu.train.business.admin.pojo.dto.query.daily.DailyTrainCarriageQueryDTO;
import com.senvu.train.business.admin.pojo.entity.TrainCarriage;
import com.senvu.train.business.admin.pojo.entity.daily.DailyTrain;
import com.senvu.train.business.admin.pojo.entity.daily.DailyTrainCarriage;
import com.senvu.train.business.admin.service.daily.DailyTrainCarriageService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DailyTrainCarriageServiceImpl implements DailyTrainCarriageService {

    @Autowired
    private DailyTrainCarriageMapper dailyTrainCarriageMapper;
    @Autowired
    private DailyTrainMapper dailyTrainMapper;
    @Autowired
    private TrainCarriageMapper trainCarriageMapper;

    @Override
    public PageResult list(DailyTrainCarriageQueryDTO dailyTrainCarriageQueryDTO) {
        Integer page = dailyTrainCarriageQueryDTO.getPage();
        Integer size = dailyTrainCarriageQueryDTO.getSize();
        Page<DailyTrainCarriage> pages = new Page<>(page,size);
        LambdaQueryWrapper<DailyTrainCarriage> wrapper = new LambdaQueryWrapper<>();
        deal(dailyTrainCarriageQueryDTO, wrapper);
        dailyTrainCarriageMapper.selectPage(pages,wrapper);
        PageResult pageResult = new PageResult();
        pageResult.setTotal(pages.getTotal());
        pageResult.setList(pages.getRecords());
        return pageResult;
    }

    private static void deal(DailyTrainCarriageQueryDTO dailyTrainCarriageQueryDTO, LambdaQueryWrapper<DailyTrainCarriage> wrapper) {
        wrapper.orderByDesc(DailyTrainCarriage::getDate).orderByAsc(DailyTrainCarriage::getTrainCode);
        if (dailyTrainCarriageQueryDTO.getDate() != null && dailyTrainCarriageQueryDTO.getTrainCode() != null){
            wrapper.eq(DailyTrainCarriage::getDate, dailyTrainCarriageQueryDTO.getDate()).eq(DailyTrainCarriage::getTrainCode, dailyTrainCarriageQueryDTO.getTrainCode());
        } else if (dailyTrainCarriageQueryDTO.getDate() != null) {
            wrapper.eq(DailyTrainCarriage::getDate, dailyTrainCarriageQueryDTO.getDate());
        } else if (dailyTrainCarriageQueryDTO.getTrainCode() != null) {
            wrapper.eq(DailyTrainCarriage::getTrainCode, dailyTrainCarriageQueryDTO.getTrainCode());
        }
    }

    @Override
    public Result getById(Long id) {
        Result result = new Result();
        result.setData(dailyTrainCarriageMapper.selectById(id));
        result.setCode(Result.SUCCESS_CODE);
        return result;
    }

    @Override
    public Result save(DailyTrainCarriageDTO dailyTrainCarriageDTO) {
          DailyTrainCarriage dailyTrainCarriage = new DailyTrainCarriage();
          BeanUtils.copyProperties(dailyTrainCarriageDTO,dailyTrainCarriage);
          if (dailyTrainCarriage.getId() != null){
              List<SeatColEnum> col = SeatColEnum.getColsByType(dailyTrainCarriageDTO.getSeatType());
              Integer row = dailyTrainCarriageDTO.getRowCount();
              dailyTrainCarriage.setColCount(col.size());
              dailyTrainCarriage.setSeatCount(col.size() * row);
              dailyTrainCarriageMapper.updateById(dailyTrainCarriage);
          } else {
              if (existed(dailyTrainCarriage)){
                  throw new BusinessException(BusinessExceptionEnum.DAILY_CARRIAGE_EXITED_ERROR);
              }
              List<SeatColEnum> col = SeatColEnum.getColsByType(dailyTrainCarriageDTO.getSeatType());
              Integer row = dailyTrainCarriageDTO.getRowCount();
              dailyTrainCarriage.setColCount(col.size());
              dailyTrainCarriage.setSeatCount(col.size() * row);
              dailyTrainCarriageMapper.insert(dailyTrainCarriage);
          }
        return new Result(Result.SUCCESS_CODE);
    }

    private boolean existed(DailyTrainCarriage dailyTrainCarriage) {
        LambdaQueryWrapper<DailyTrainCarriage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DailyTrainCarriage::getTrainCode, dailyTrainCarriage.getTrainCode()).eq(DailyTrainCarriage::getIndex, dailyTrainCarriage.getIndex()).eq(DailyTrainCarriage::getDate,dailyTrainCarriage.getDate());
        return !dailyTrainCarriageMapper.selectList(wrapper).isEmpty();
    }

    @Override
    public Result update(DailyTrainCarriageDTO dailyTrainCarriageDTO) {
        DailyTrainCarriage dailyTrainCarriage = new DailyTrainCarriage();
        BeanUtils.copyProperties(dailyTrainCarriageDTO,dailyTrainCarriage);
     //   dailyTrainCarriageMapper.updateById();
        return new Result(Result.SUCCESS_CODE);
    }

    @Override
    public Result delete(Long id) {
        dailyTrainCarriageMapper.deleteById(id);
        return new Result(Result.SUCCESS_CODE);
    }

    @Override
    public void genCarriage(LocalDate date) {
        // 1. 查出当日的所有车次
        LambdaQueryWrapper<DailyTrain> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DailyTrain::getDate, date);
        List<DailyTrain> dailyTrains = dailyTrainMapper.selectList(wrapper);
        // 2. 对所有车次查出车厢进行添加
        for (DailyTrain dailyTrain : dailyTrains) {
            LambdaQueryWrapper<DailyTrainCarriage> wrapper2 = new LambdaQueryWrapper<>();
            wrapper2.eq(DailyTrainCarriage::getTrainCode, dailyTrain.getCode());
            dailyTrainCarriageMapper.delete(wrapper2);

            LambdaQueryWrapper<TrainCarriage> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.eq(TrainCarriage::getTrainCode, dailyTrain.getCode());
            List<TrainCarriage> trainCarriages = trainCarriageMapper.selectList(wrapper1);
            for (TrainCarriage trainCarriage : trainCarriages) {
                DailyTrainCarriage dailyTrainCarriage = new DailyTrainCarriage();
                BeanUtils.copyProperties(trainCarriage, dailyTrainCarriage);
                dailyTrainCarriage.setDate(date);
                dailyTrainCarriageMapper.insert(dailyTrainCarriage);
            }
        }
    }
}
