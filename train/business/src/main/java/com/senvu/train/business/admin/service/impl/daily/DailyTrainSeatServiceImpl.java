package com.senvu.train.business.admin.service.impl.daily;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senvu.result.PageResult;
import com.senvu.result.Result;
import com.senvu.train.business.admin.mapper.TrainCarriageMapper;
import com.senvu.train.business.admin.mapper.TrainSeatMapper;
import com.senvu.train.business.admin.mapper.TrainStationMapper;
import com.senvu.train.business.admin.mapper.daily.DailyTrainCarriageMapper;
import com.senvu.train.business.admin.mapper.daily.DailyTrainSeatMapper;
import com.senvu.train.business.admin.pojo.dto.daily.DailyTrainSeatDTO;
import com.senvu.train.business.admin.pojo.dto.query.daily.DailyTrainSeatQueryDTO;
import com.senvu.train.business.admin.pojo.entity.TrainSeat;
import com.senvu.train.business.admin.pojo.entity.TrainStation;
import com.senvu.train.business.admin.pojo.entity.daily.DailyTrainCarriage;
import com.senvu.train.business.admin.pojo.entity.daily.DailyTrainSeat;
import com.senvu.train.business.admin.service.daily.DailyTrainSeatService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DailyTrainSeatServiceImpl implements DailyTrainSeatService {

    @Autowired
    private DailyTrainSeatMapper dailyTrainSeatMapper;
    @Autowired
    private TrainCarriageMapper trainCarriageMapper;
    @Autowired
    private TrainSeatMapper trainSeatMapper;
    @Autowired
    private DailyTrainCarriageMapper dailyTrainCarriageMapper;
    @Autowired
    private TrainStationMapper trainStationMapper;

    @Override
    public PageResult list(DailyTrainSeatQueryDTO dailyTrainSeatQueryDTO) {
        Integer page = dailyTrainSeatQueryDTO.getPage();
        Integer size = dailyTrainSeatQueryDTO.getSize();
        Page<DailyTrainSeat> pages = new Page<>(page,size);
        LambdaQueryWrapper<DailyTrainSeat> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(DailyTrainSeat::getTrainCode).orderByAsc(DailyTrainSeat::getCarriageIndex).orderByAsc(DailyTrainSeat::getCarriageSeatIndex);
        if (dailyTrainSeatQueryDTO.getTrainCode() != null) {
            wrapper.eq(DailyTrainSeat::getTrainCode,dailyTrainSeatQueryDTO.getTrainCode());
        }
        dailyTrainSeatMapper.selectPage(pages,wrapper);
        PageResult pageResult = new PageResult();
        pageResult.setTotal(pages.getTotal());
        pageResult.setList(pages.getRecords());
        return pageResult;
    }

    @Override
    public Result getById(Long id) {
        Result result = new Result();
        result.setData(dailyTrainSeatMapper.selectById(id));
        result.setCode(Result.SUCCESS_CODE);
        return result;
    }

    @Override
    public Result save(DailyTrainSeatDTO dailyTrainSeatDTO) {
          DailyTrainSeat dailyTrainSeat = new DailyTrainSeat();
          BeanUtils.copyProperties(dailyTrainSeatDTO,dailyTrainSeat);
     //   dailyTrainSeatMapper.insert();
        return new Result(Result.SUCCESS_CODE);
    }

    @Override
    public Result update(DailyTrainSeatDTO dailyTrainSeatDTO) {
        DailyTrainSeat dailyTrainSeat = new DailyTrainSeat();
        BeanUtils.copyProperties(dailyTrainSeatDTO,dailyTrainSeat);
     //   dailyTrainSeatMapper.updateById();
        return new Result(Result.SUCCESS_CODE);
    }

    @Override
    public Result delete(Long id) {
        dailyTrainSeatMapper.deleteById(id);
        return new Result(Result.SUCCESS_CODE);
    }

    @Override
    public void genSeat(LocalDate date) {
        // 1. 删除对应座位
        // 2. 查找每日车厢
        LambdaQueryWrapper<DailyTrainCarriage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DailyTrainCarriage::getDate,date);
        List<DailyTrainCarriage> dailyTrainCarriages = dailyTrainCarriageMapper.selectList(wrapper);
        // 3. 生成座位
        for (DailyTrainCarriage dailyTrainCarriage : dailyTrainCarriages) {
            LambdaQueryWrapper<DailyTrainSeat> wrapper2 = new LambdaQueryWrapper<>();
            wrapper2.eq(DailyTrainSeat::getTrainCode, dailyTrainCarriage.getTrainCode()).eq(DailyTrainSeat::getCarriageIndex, dailyTrainCarriage.getIndex());
            dailyTrainSeatMapper.delete(wrapper2);
            LambdaQueryWrapper<TrainSeat> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.eq(TrainSeat::getTrainCode, dailyTrainCarriage.getTrainCode()).eq(TrainSeat::getCarriageIndex, dailyTrainCarriage.getIndex());
            List<TrainSeat> trainSeats = trainSeatMapper.selectList(wrapper1);
            for (TrainSeat trainSeat : trainSeats) {
                DailyTrainSeat dailyTrainSeat = new DailyTrainSeat();
                BeanUtils.copyProperties(trainSeat, dailyTrainSeat);
                dailyTrainSeat.setDate(date);
                dailyTrainSeat.setSell(StrUtil.fillBefore("", '0', trainStationMapper.selectList(new LambdaQueryWrapper<TrainStation>().eq(TrainStation::getTrainCode, trainSeat.getTrainCode())).size() - 1));
                dailyTrainSeatMapper.insert(dailyTrainSeat);
                }
        }
    }
}
